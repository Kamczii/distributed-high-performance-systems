<template>
  <h1>TRAVEL OFFICE</h1>
  <div class="layout">
    <div>
      <OfferDebug></OfferDebug>
      <InitButtons></InitButtons>
    </div>
    <div><SearchComponent/></div>
    <div><LastUpdates/></div>
  </div>
</template>

<script setup>

import SearchComponent from "@/components/search/SearchComponent.vue";
import LastUpdates from "@/components/LastUpdates";
import InitButtons from "@/components/init/InitButtons.vue";
import OfferDebug from "@/components/debug/OfferDebug.vue";
</script>

<style scoped>

.layout {
  display: grid;
  grid-template-columns: .5fr 3fr 1fr;
  grid-gap: .5rem;
}

.layout > div {
  padding: .5rem;
}



</style>