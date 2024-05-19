<template>
  <div>
    <h2>flights</h2>
    <ul>
      <li v-for="flight in flights" :key="flight.id">
        <pre>{{ flight }}</pre>
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: 'flightList',
  data() {
    return {
      flights: []
    };
  },
  mounted() {
    this.fetchflights();
  },
  methods: {
    fetchflights() {
      fetch(process.env.VUE_APP_GATEWAY + "/test/flights")
          .then(res => res.json())
          .then(data => this.flights = data)
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
