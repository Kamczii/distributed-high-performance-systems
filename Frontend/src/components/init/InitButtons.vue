<script>
import PopupComponent from "@/components/PopupComponent.vue";
import {ref} from "vue";
import {useRoute} from 'vue-router';

export default {
  components: {PopupComponent},
  setup() {
    const route = useRoute();
    const popupMessage = ref('');
    const requestLimit = ref(300); // Default limit value

    const isAdmin = ref(route.query.admin === 'true');

    const init = () => {
      fetch(process.env.VUE_APP_GATEWAY + '/init')
          .then(res => res.text())
          .then(data => popupMessage.value = data)
          .catch(err => console.error(err));
    };

    const flights = () => {
      fetch(`${process.env.VUE_APP_GATEWAY}/flights/send?limit=${requestLimit.value}`)
          .then(res => res.text())
          .then(data => popupMessage.value = data)
          .catch(err => console.error(err));
    };

    const busses = () => {
      fetch(`${process.env.VUE_APP_GATEWAY}/busses/send?limit=${requestLimit.value}`)
          .then(res => res.text())
          .then(data => popupMessage.value = data)
          .catch(err => console.error(err));
    };

    const hotels = () => {
      fetch(`${process.env.VUE_APP_GATEWAY}/hotels/send?limit=${requestLimit.value}`)
          .then(res => res.text())
          .then(data => popupMessage.value = data)
          .catch(err => console.error(err));
    };

    const start_updating = () => {
      fetch(process.env.VUE_APP_GATEWAY + '/update/start')
          .then(res => res.text())
          .then(data => popupMessage.value = data)
          .catch(err => console.error(err));
    };

    const stop_updating = () => {
      fetch(process.env.VUE_APP_GATEWAY + '/update/stop')
          .then(res => res.text())
          .then(data => popupMessage.value = data)
          .catch(err => console.error(err));
    };

    const resetPopupMessage = () => {
      popupMessage.value = '';
    };

    return {
      popupMessage,
      requestLimit,
      isAdmin,
      init,
      flights,
      busses,
      hotels,
      start_updating,
      stop_updating,
      resetPopupMessage
    };
  }
}
</script>

<template>
  <nav>
    <h3>Tour operator</h3>
    <ul>
      <li>
        <label for="limit">Request Limit: </label>
        <input type="number" v-model="requestLimit" id="limit" />
      </li>
      <template v-if="isAdmin">
        <li class="button" @click="init">Init</li>
        <li class="button" @click="flights">Flight publish</li>
        <li class="button" @click="busses">Bus publish</li>
        <li class="button" @click="hotels">Hotel publish</li>
        <li class="button" @click="start_updating">Start updating prices</li>
        <li class="button" @click="stop_updating">Stop updating prices</li>
      </template>
    </ul>
  </nav>
  <PopupComponent :message="popupMessage" @reset-message="resetPopupMessage"></PopupComponent>
</template>

<style scoped>
ul {
  margin: 0;
  list-style-type: none;
  padding: 0;
}
li {
  cursor: pointer;
  margin: 10px 0;
}
input {
  margin-left: 10px;
}
</style>
