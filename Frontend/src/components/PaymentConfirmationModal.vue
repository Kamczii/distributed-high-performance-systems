<!-- PaymentConfirmationModal.vue -->
<template>
  <div class="modal-overlay">
    <div class="modal-content" v-if="!isLoading">
      <h3>Payment Confirmation</h3>
      <p>Are you sure you want to proceed with the payment?</p>
      <div class="modal-buttons">
        <button @click="acceptPayment">Accept & Pay</button>
        <button @click="cancelPayment">Cancel</button>
      </div>
    </div>
    <div class="modal-content" v-else>
      <div class="loading-animation">
        <div class="loading-circle"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isLoading: {
      type: Boolean,
      default: true,
      required: false
    },
    paymentId: {
      type: String,
      required: false
    },
    orderId: {
      type: String,
      required: false
    }
  },
  emits: ['accept', 'cancel'],
  methods: {
    acceptPayment() {
      this.$emit('accept');
      this.sendChoice(process.env.VUE_APP_GATEWAY  + `/payment/accept`)
    },
    cancelPayment() {
      this.$emit('cancel');
      this.sendChoice(process.env.VUE_APP_GATEWAY  + `/payment/cancel`)
    },
    sendChoice(url) {
      const data = {
        orderId: this.props.orderId
      };
      const config = {
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
          'userId': '10'
        }
      };
      fetch(url, {
        method: 'POST',
        body: JSON.stringify(data),
        ...config
      })
          .then(res => res.text())
          .then(data => console.log(data))
          .catch(err => console.error(err));
    }
  },
  watch: {
    paymentId(newVal) {
      if (newVal != null) this.props.isLoading.value = false;
    }
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
}

.modal-buttons button {
  margin: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.modal-buttons button:first-child {
  background-color: #42b883;
  color: white;
}

.modal-buttons button:last-child {
  background-color: #ff5f5f;
  color: white;
}

.loading-animation {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 0 auto;
}

.loading-circle {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: 6px solid #007bff;
  border-top-color: #fff;
  animation: loading-spin 1s ease-in-out infinite;
}

@keyframes loading-spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
