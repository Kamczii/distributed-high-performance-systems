<script>
import PopupComponent from "@/components/PopupComponent.vue";
import {ref} from "vue";

export default {
  components: {PopupComponent},
  data() {
    return {
      popupMessage: ref('')
    }
  },
  methods: {
    init() {
      fetch(process.env.VUE_APP_GATEWAY + '/init')
          .then(res => res.text())
          .then(data => this.popupMessage = data)
          .catch(err => console.error(err));
    },
    flights() {
      fetch(process.env.VUE_APP_GATEWAY + '/flights/send?limit=100')
          .then(res => res.text())
          .then(data => this.popupMessage = data)
          .catch(err => console.error(err));
    },
    busses() {
      fetch(process.env.VUE_APP_GATEWAY + '/busses/send?limit=100')
          .then(res => res.text())
          .then(data => this.popupMessage = data)
          .catch(err => console.error(err));
    },
    hotels() {
      fetch(process.env.VUE_APP_GATEWAY + '/hotels/send?limit=100')
          .then(res => res.text())
          .then(data => this.popupMessage = data)
          .catch(err => console.error(err));
    },
    resetPopupMessage() {
      this.popupMessage = '';
    }
  }
}
</script>

<template>
  <nav>
    <h3>Tour operator</h3>
    <ul>
      <li class="button" @click="init">Init</li>
      <li class="button" @click="flights">Flight publish</li>
      <li class="button" @click="busses">Bus publish</li>
      <li class="button" @click="hotels">Hotel publish</li>
    </ul>
  </nav>
  <PopupComponent :message="popupMessage" @reset-message="resetPopupMessage"></PopupComponent>
</template>

<style scoped>
ul {
  margin: 0;
}
li {
  cursor: pointer;
}
</style>