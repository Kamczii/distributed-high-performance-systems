<template>
  <div class="offer-short-info" v-if="offer">
    <h2>Offer Details</h2>
    <h3>Hotel Information</h3>
    <p>Status: {{offer.status}}</p>
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


    <button type="submit">Buy now!</button>
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
    fetch("http://localhost:8081/offers/" + this.$route.params.id)
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
  max-width: 400px;
}
h2, h3 {
  color: #333;
}
p {
  color: #666;
  font-size: 16px;
  margin: 5px 0;
}

button {
  background-color: #42b883;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
  margin: 10px 0 5px;
}

button:hover {
  background-color: #35495e;
}
</style>
