<template>
  <h1>Travel office</h1>
  <div class="layout">
  <div><SearchComponent/></div>
  <div><LastUpdates/></div>
  </div>
</template>
<script>
import SearchComponent from "@/components/search/SearchComponent.vue";
import LastUpdates from "@/components/LastUpdates";

export default {
  name: 'App',
  components: {
    LastUpdates,
    SearchComponent,
  }
}
</script>

<style>

h1 {
  text-align: center;
}

.layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  grid-gap: 1rem;
}

.layout > div {
  padding: 1rem;
}
</style>
