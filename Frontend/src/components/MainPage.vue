<script setup>
import SearchComponent from "@/components/search/SearchComponent.vue";
import LastUpdates from "@/components/LastUpdates";
</script>

<template>
  <h1>Travel office</h1>
  <div class="layout">
    <nav>
      <ul>
        <li><router-link to="/flights">Flights</router-link></li>
        <li><router-link to="/hotels">Hotels</router-link></li>
        <li><router-link to="/offers">Offers</router-link></li>
      </ul>
    </nav>
    <div><SearchComponent/></div>
    <div><LastUpdates/></div>
  </div>
</template>

<style scoped>
nav ul {
  list-style: none;
  margin: 0;
  padding: 0;
}
nav li {
  margin-right: 10px; /* Adjust spacing between items as needed */
  background-color: #42b883;
  color: #f7f7f7;
}
a {
  text-decoration: none;
  color: #f7f7f7;
}
</style>