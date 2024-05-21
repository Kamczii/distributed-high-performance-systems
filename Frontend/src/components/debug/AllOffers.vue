<template>
  <div>
    <h2>Offers</h2>
    <ul>
      <li v-for="offer in offers" :key="offer.id">
        <pre>{{ offer }}</pre>
      </li>
    </ul>
  </div>
</template>

<script>

export default {
  name: 'OfferList',
  data() {
    return {
      offers: []
    };
  },
  mounted() {
    this.fetchOffers();
  },
  methods: {
    fetchOffers() {
      fetch(process.env.VUE_APP_GATEWAY + "/test/offers")
          .then(res => res.json())
          .then(data => this.offers = data)
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
