<template>
  <div class="content-wrapper">
    <!-- Offer Details -->
  <div class="offer-short-info" v-if="offer">
    <ConfigurationComponent @update-kids="handleKidsUpdate"/>
    <h2>Offer Details</h2>
    <h3>Hotel Information</h3>
    <p>Status: {{ offer.status }}</p>
    <p>Transport: {{ offer.transport }}</p>
    <p>Name: {{ offer.hotel.name }}</p>
    <p>Room Type: {{ offer.hotel.room.type }}</p>
    <p>Persons: {{ offer.hotel.room.capacity }}</p>
    <p>Number of Beds: {{ offer.hotel.room.beds }}</p>

      <h3>Flight Details</h3>
      <p>Departure: {{ offer.departure.city }} / {{ offer.departure.country }}</p>
      <p>Destination: {{ offer.destination.city }} / {{ offer.destination.country }}</p>

      <h3>Date Information</h3>
      <p>Start Date: {{ formatDate(offer.start) }}</p>
      <p>End Date: {{ formatDate(offer.end) }}</p>
      <p>Price: {{ offer.price }} zł</p>

      <PopupComponent :message="popupMessage" @reset-message="resetPopupMessage" />
      <button type="submit" class="buy-now-button" @click="createOrder">Buy now!</button>

      <PaymentConfirmationModal
          v-if="isPaymentModalVisible"
          :order-id="orderId"
          :payment-id="paymentId"
          @accept="acceptPayment"
          @cancel="cancelPayment"
      />
    </div>

    <!-- Updates List -->
    <div class="updates-list">
      <h1>Last updates</h1>
      <div v-for="(item, index) in items" :key="index" class="card">
        <h3>{{ item.title }}</h3>
        <p>OfferId: {{ item.description }}</p>
        <p>{{ item.timestamp }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted , onBeforeUnmount} from 'vue';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import PopupComponent from "@/components/PopupComponent.vue";
import ConfigurationComponent from "@/components/search/ConfigurationComponent.vue";
import PaymentConfirmationModal from "@/components/PaymentConfirmationModal.vue";
import {useRoute} from 'vue-router';
export default {
  components: {
    PopupComponent,
    ConfigurationComponent,
    PaymentConfirmationModal
  },
  watch: {
    "$route.query": {
      immediate: true,
      handler(newQuery) {
        const params = new URLSearchParams(newQuery).toString()
        fetch(process.env.VUE_APP_GATEWAY + `/offers/${this.$route.params.id}?${params}` )
            .then(res => res.json())
            .then(data => this.offer = data)
            .catch(err => console.log(err))
      }
    }
  },
  setup() {
    const popupMessage = ref('');
    const subscription = ref(null);
    const subscriptionPayment = ref(null);
    const offer = ref(null);
    const stompClient = ref(null);
    const route = useRoute();
    const kids = ref([]);
    const isPaymentModalVisible = ref(false);
    const orderId = ref(null);
    const paymentId = ref(null);
    const items = ref([]);
    onMounted(() => {
      fetch(process.env.VUE_APP_GATEWAY + `/offers/${route.params.id}`)
          .then(res => res.json())
          .then(data => {
            offer.value = data;
          })
          .catch(err => console.error(err));
      connectWebSocket();
    });
    function handleKidsUpdate(kidsArray) {
      kids.value = kidsArray;
    }
    onBeforeUnmount(() => {
      if (subscription.value) {
        subscription.value.unsubscribe(); // Unsubscribe when component unmounts
        console.log('Unsubscribed from the topic');
      }
    });
    function formatDate(value) {
      return value ? new Date(value).toLocaleDateString() : '';
    }
    function connectWebSocket() {
      const socket = new SockJS(process.env.VUE_APP_WS_GATEWAY + '/ws');
      stompClient.value = Stomp.over(socket);
      stompClient.value.connect({}, () => {
        subscription.value = stompClient.value.subscribe(`/topic/notifications/${route.params.id}`, notification => {
          const event = JSON.parse(notification.body);
          items.value.unshift(event); // Add to the start of the list
          if (items.value.length > 10) {
            items.value.pop(); // Remove the oldest item if the list exceeds 10
          }
          console.log(items);
          popupMessage.value = event.title;
          console.log('Received message:', event);
          fetchOfferDetails();
        });
      }, error => {
        console.error('Error connecting to WebSocket:', error);
      });
    }
    function fetchOfferDetails() {
      console.log('Received message:',route.params.id);
      fetch(process.env.VUE_APP_GATEWAY + `/offers/${route.params.id}`)
        .then(res => res.json())
        .then(data => {
          offer.value = data;
        })
        .catch(err => {
          console.error('Error fetching offer details:', err);
        });
    }
    function resetPopupMessage() {
      popupMessage.value = '';
    }
    function showPaymentConfirmation() {
      isPaymentModalVisible.value = true;
    }
    function cancelPayment() {
      isPaymentModalVisible.value = false;
    }
    function acceptPayment() {
      isPaymentModalVisible.value = false;
      createOrder();
    }
    function createOrder() {
      showPaymentConfirmation()
      for (let i = 0; i < kids.value.length; i++) {
        kids.value[i] = calculateAge(kids.value[i]);
      }
      console.log('Kids ages:', kids.value);
      const offerId = route.params.id;
      const url = process.env.VUE_APP_GATEWAY  + `/order/create`;
      const data = {
        offerId: offerId,
        ageOfVisitors: kids.value
      };
      const config = {
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
          'userId': '10'
        }
      };
      fetch(url, {
        method: 'POST',
        body: JSON.stringify(data),
        ...config
      })
          .then(res => res.text())
          .then(data => orderId.value = data)
          .catch(err => console.error(err));
        connectPaymentWebSocket();
    }
    function calculateAge(birthDate) {
      const today = new Date();
      const birthDateObj = new Date(birthDate);
      let age = today.getFullYear() - birthDateObj.getFullYear();
      if (
          today.getMonth() < birthDateObj.getMonth() ||
          (today.getMonth() === birthDateObj.getMonth() && today.getDate() < birthDateObj.getDate())
      ) {
        age--;
      }
      console.log(age.toString())
      return age.toString();
    }
    function connectPaymentWebSocket(){
            console.log(orderId.value);
            subscriptionPayment.value = stompClient.value.subscribe(`/topic/notifications/payment/${orderId.value}`, notification => {
              const event = JSON.parse(notification.body);
              paymentId.value = event;
              unsubscribePayment(subscriptionPayment);
            });
    }
    function unsubscribePayment(subscriptionRef) {
               if (subscriptionRef) {
                 subscriptionRef.value.unsubscribe();
                 console.log('Unsubscribed from topic successfully.');
               }
             }
    return { stompClient,formatDate, resetPopupMessage, createOrder, cancelPayment, acceptPayment, popupMessage, offer, isPaymentModalVisible, handleKidsUpdate, paymentId, orderId, items, connectPaymentWebSocket };
  }
}
</script>

<style scoped>
.content-wrapper {
  display: grid;
  grid-template-columns: .9fr 1fr;
  grid-gap: .5rem;
}
.content-wrapper > div {
  padding: .5rem;
}
.offer-short-info {
  flex: 1; /* Less space relative to updates */
  padding-right: 20px; /* Space between offer details and updates */
}
.updates-list {
  flex: 2; /* More space allocated to updates */
  max-width: 400px; /* Adjust maximum width as needed */
}
.buy-now-button {
  background-color: #42b883;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.buy-now-button:hover {
  background-color: #35495e;
}
.card-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f7f7f7;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.card {
  background-color: #42b883; /* Vue Green */
  color: #ffffff; /* White */
  padding: 20px;
  margin: 10px 0;
  width: 100%; /* Ensures full usage of the allocated flex space */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}
.card h3 {
  color: #35495e; /* Dark Green */
  margin: 0 0 10px 0;
}
</style>