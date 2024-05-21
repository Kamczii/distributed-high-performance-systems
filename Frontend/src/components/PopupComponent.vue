<template>
  <div v-if="show" class="popup">
    <span>{{ message }}</span>
    <button @click="close">Close</button>
  </div>
</template>

<script>
export default {
  props: ['message'],
  data() {
    return {
      show: false
    }
  },
  watch: {
    message(newVal) {
      if (newVal !== '') {
        console.log('Received message in PopupComponent:', newVal);
        this.show = true;
      }
    }
  },
  methods: {
    close() {
      this.show = false;
      this.$emit('reset-message');
    }
  }
}
</script>

<style scoped>
.popup {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translateX(-50%);
  background-color: #42b883; /* Vue Green */
  color: #ffffff; /* White */
  border-radius: 5px;
  padding: 20px;
  z-index: 100;
  display: flex;
  flex-direction: column;
  align-items: center; /* Opcjonalne, aby wyśrodkować zawartość */
  -webkit-box-shadow: 8px 8px 24px 0px rgba(66, 68, 90, 1);
  -moz-box-shadow: 8px 8px 24px 0px rgba(66, 68, 90, 1);
  box-shadow: 8px 8px 24px 0px rgba(66, 68, 90, 1);
}
</style>
