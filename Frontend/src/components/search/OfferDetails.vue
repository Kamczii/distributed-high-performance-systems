<template>
  <div class="offer-short-info" v-if="offer">
    <h2>Offer Details</h2>
    <h3>Hotel Information</h3>
    <p>Name: {{ offer.hotel.name }}</p>
    <p>Room Type: {{ offer.hotel.room.type }}</p>
    <p>Room Capacity: {{ offer.hotel.room.capacity }}</p>
    <p>Number of Beds: {{ offer.hotel.room.beds }}</p>

    <h3>Travel Details</h3>
    <p>Departure City: {{ offer.departure.city }}</p>
    <p>Departure Country: {{ offer.departure.country }}</p>
    <p>Destination City: {{ offer.destination.city }}</p>
    <p>Destination Country: {{ offer.destination.country }}</p>

    <h3>Date Information</h3>
    <p>Start Date: {{ formatDate(offer.start) }}</p>
    <p>End Date: {{ formatDate(offer.end) }}</p>
  </div>
</template>

<script>
export default {
  name: 'OfferShortInfo',
  props: ['id'],
  data() {
    return {
      offer: null
    }
  },
  methods: {
    formatDate(value) {
      return value ? new Date(value).toLocaleDateString() : '';
    }
  },
  mounted() {
    fetch("http://localhost:8081/offers/" + this.id)
        .then(res => res.json())
        .then(data => this.offer = data)
        .catch(err => console.log(err))
  }
}
</script>

<style scoped>
.offer-short-info {
  font-family: 'Arial', sans-serif;
  margin: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
h2, h3 {
  color: #333;
}
p {
  color: #666;
  font-size: 16px;
  margin: 5px 0;
}
</style>
