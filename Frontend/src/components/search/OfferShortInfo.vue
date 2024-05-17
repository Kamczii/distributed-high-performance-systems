<template>
  <router-link :to="{name: 'OfferDetails', params: {id: offer.id}}">
    <div class="offer-short-info">
      <div><strong>Hotel:</strong> {{ offer.hotel.name }} - {{ offer.hotel.room.type }} ({{ offer.hotel.room.capacity }} persons, {{ offer.hotel.room.beds }} beds)</div>
      <div><strong>Travel:</strong> From {{ offer.departure.city }} / {{ offer.departure.country }} to {{ offer.destination.city }} / {{ offer.destination.country }}</div>
      <div><strong>Dates:</strong> {{ formatDate(offer.start) }} to {{ formatDate(offer.end) }}</div>
      <div  v-if="offer.price"><strong>Price:</strong> {{ offer.price }} </div>
    </div>
  </router-link>
</template>

<script>
export default {
  name: 'OfferShortInfo',
  props: {
    offer: {
      type: Object,
      required: true
    }
  },
  methods: {
    formatDate(value) {
      return value ? new Date(value).toLocaleDateString() : '';
    }
  }
}
</script>

<style scoped>
.offer-short-info {
  font-family: 'Arial', sans-serif;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
  color: black;
}
.offer-short-info div {
  margin: 5px 0;
  font-size: 14px;
}
strong {
  color: #333;
}
</style>
