<template>
  <div>
    <h1>Available Offers</h1>
    <div v-for="offer in offers" :key="offer.id">
      <offer-short-info :offer="offer" />
    </div>
  </div>
</template>

<script>
import OfferShortInfo from './OfferShortInfo.vue';

export default {
  name: 'OfferList',
  components: {
    OfferShortInfo
  },
  data() {
    return {
      offers: []
    };
  },
  mounted() {
    fetch("http://localhost:8081/offers")
        .then(res => res.json())
        .then(data => this.offers = data.content)
        .catch(err => console.log(err))
  },
  watch: {
    '$route.query': {
      immediate: true, // Trigger the handler immediately with the current route
      handler(newQuery) {
        const params = new URLSearchParams(newQuery).toString()
        fetch(`http://localhost:8081/offers?${params}` )
            .then(res => res.json())
            .then(data => this.offers = data.content.sort())
            .catch(err => console.log(err))
      }
    }
  }
}
</script>

<style>
h1 {
  font-family: 'Arial', sans-serif;
  color: #333;
  padding: 20px;
}
</style>
