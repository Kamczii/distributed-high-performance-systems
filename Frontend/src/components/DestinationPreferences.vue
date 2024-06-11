<template>
   <div class="card-list">
   <h3>Popular Destinations</h3>
     <div v-if="computedItems.length > 0">
       <div v-for="(item, index) in computedItems" :key="index" class="card">
         <p>{{ item.description }}</p>
       </div>
     </div>
   </div>
 </template>
 <script>
 import SockJS from 'sockjs-client';
 import Stomp from 'webstomp-client';
 export default{
  data() {
    return {
      items: [],
      stompClient: null,
      subscribed: false, // Ustaw wartość domyślną, aby uniknąć niezdefiniowanego błędu
    };
  },
   computed: {
     computedItems() {
       return this.items;
     }
   },
 beforeUnmount() {
   if (this.stompClient && this.subscribed) {
     this.stompClient.disconnect(() => {
       console.log('Disconnected from WebSocket');
       this.subscribed= false;
     });
   }
 },
 mounted() {
     // Set up WebSocket connection
     const socket = new SockJS(process.env.VUE_APP_WS_GATEWAY + '/ws');
     this.stompClient = Stomp.over(socket);
     this.stompClient.connect({}, () => {
       this.stompClient.subscribe('/topic/preferences/destinations', hotel => {
         this.subscribed= true;
         const event = JSON.parse(hotel.body);
         this.updateItems(event);
         console.log('Received message:', event);
         console.log(this.items);
       });
     }, error => {
       console.error('Error connecting to WebSocket:', error);
     });
   },
   methods: {
     updateItems(event) {

       if (this.items.length === 0 || !this.items.some(item => item.description === event.description)) {
         this.items.unshift(event);
       }

       if (this.items.length > 5) {
         this.items.pop();
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
Empty file modified