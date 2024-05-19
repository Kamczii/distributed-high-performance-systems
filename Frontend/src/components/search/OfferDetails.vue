<template>
  <div class="offer-short-info" v-if="offer">
    <ConfigurationComponent />
    <h2>Offer Details</h2>
    <h3>Hotel Information</h3>
    <p>Status: {{ offer.status }}</p>
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
    <button type="submit" @click="createOrder">Buy now!</button>
  </div>
</template>

<script>
import {onMounted, ref} from 'vue';
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
import PopupComponent from "@/components/PopupComponent.vue";
import ConfigurationComponent from "@/components/search/ConfigurationComponent.vue";
import {useRoute} from 'vue-router';

export default {
  components: {
    PopupComponent,
    ConfigurationComponent
  },
  setup() {
    const popupMessage = ref('');
    const offer = ref(null);
    const stompClient = ref(null);
    const route = useRoute();

    onMounted(() => {
      fetch(`http://localhost:8081/offers/${route.params.id}`)
        .then(res => res.json())
        .then(data => {
          offer.value = data;
        })
        .catch(err => console.error(err));

      connectWebSocket();
    });

    function formatDate(value) {
      return value ? new Date(value).toLocaleDateString() : '';
    }

    function connectWebSocket() {
      const socket = new SockJS(process.env.VUE_APP_GATEWAY + '/ws');
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

    function createOrder() {
      const offerId = route.params.id;
      const url = `http://localhost:8086/order/create`;
      const data = {
        offerId: offerId,
        ageOfVisitors: [18, 20, 22]
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
        .then(data => console.log(data))
        .catch(err => console.error(err));
    }

    return { formatDate, resetPopupMessage, createOrder, popupMessage, offer };
  }
}
</script>

<style scoped>
/* Twoje style */
</style>
