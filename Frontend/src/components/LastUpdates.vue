<template>
  <div class="card-list">
  <h1>Last 10 updates</h1>
    <div v-for="(item, index) in items" :key="index" class="card">
      <h3>{{ item.title }}</h3>
      <p>OfferId: {{ item.description }}</p>
      <p>{{item.timestamp}}</p>
    </div>
  </div>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

export default {
  data() {
    return {
      items: [],
      stompClient: null // Dodano definicję zmiennej, aby była dostępna w całym komponencie
    };
  },
  mounted() {
    // Fetch initial messages using fetch
    fetch('http://localhost:8083/initial')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Network response was not ok.');
      })
      .then(data => {
        this.items = data;
      })
      .catch(error => {
        console.error('Error fetching initial messages:', error);
      });

    // Set up WebSocket connection
    const socket = new SockJS('http://localhost:8083/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, () => {
      this.stompClient.subscribe('/topic/notifications', notification => {
        const event = JSON.parse(notification.body);
        this.updateItems(event);
        console.log('Received message:', event);
      });
    }, error => {
      console.error('Error connecting to WebSocket:', error);
    });
  },
  methods: {
    updateItems(event) {
      this.items.unshift(event); // Add to the start of the list
      if (this.items.length > 10) {
        this.items.pop(); // Remove the oldest item if the list exceeds 10
      }
    }
  }

};
</script>


<style scoped>
.card-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f7f7f7;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.card {
  background-color: #42b883; /* Vue Green */
  color: #ffffff; /* White */
  padding: 20px;
  margin: 10px 0;
  width: 80%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.card h3 {
  color: #35495e; /* Dark Green */
  margin: 0 0 10px 0;
}
</style>
