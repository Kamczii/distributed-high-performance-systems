<script>
export default {

}
</script>

<template>
  <nav>
    <h3>Offer debug</h3>
    <ul>
      <li class="button"><router-link to="/flights">Flights</router-link></li>
      <li class="button"><router-link to="/hotels">Hotels</router-link></li>
      <li class="button"><router-link to="/offers">Offers</router-link></li>
    </ul>
  </nav>
</template>

<style scoped>
ul {
  margin: 0;
}
li {
  cursor: pointer;
}
</style>