<template>
  <div>
    <h2>hotels</h2>
    <ul>
      <li v-for="hotel in hotels" :key="hotel.id">
        <pre>{{ hotel }}</pre>
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: 'hotelList',
  data() {
    return {
      hotels: []
    };
  },
  mounted() {
    this.fetchhotels();
  },
  methods: {
    fetchhotels() {
      fetch(process.env.VUE_APP_GATEWAY + "/test/hotels")
          .then(res => res.json())
          .then(data => this.hotels = data)
          .catch(err => console.log(err))
    }
  }
}
</script>

<style>
li {
  list-style: none;
  background: #cccccc;
  padding: 5px;
  border-radius: 5px;
  margin-bottom: 10px;
}
</style>
