<template>
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
    <p>Cena: {{ offer.price }} zł</p>

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
</template>

<script>
import {onMounted, ref} from 'vue';
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
    const offer = ref(null);
    const stompClient = ref(null);
    const route = useRoute();
    const kids = ref([]);
    const isPaymentModalVisible = ref(false);
    const orderId = ref(null);
    const paymentId = ref(null);

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

    function formatDate(value) {
      return value ? new Date(value).toLocaleDateString() : '';
    }

    function connectWebSocket() {
      const socket = new SockJS(process.env.VUE_APP_WS_GATEWAY + '/ws');
      stompClient.value = Stomp.over(socket);
      stompClient.value.connect({}, () => {
        stompClient.value.subscribe(`/topic/notifications/${route.params.id}`, notification => {
          const event = JSON.parse(notification.body);
          popupMessage.value = event.title;
          console.log('Received message:', event);
        });
      }, error => {
        console.error('Error connecting to WebSocket:', error);
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

    return { formatDate, resetPopupMessage, createOrder, cancelPayment, acceptPayment, popupMessage, offer, isPaymentModalVisible, handleKidsUpdate, paymentId, orderId };
  }
}
</script>

<style scoped>
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
</style>
