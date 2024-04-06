<template>
  <div class="card-list">
  <h1>Last 10 updates (TODO)</h1>
    <div v-for="(item, index) in items" :key="index" class="card">
      <h3>{{ item.title }}</h3>
      <p>{{ item.description }}</p>
      <p>{{item.datetime}}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      items: [
        { title: 'Event 1', description: 'This is event referring to offer  1.', datetime: '01-02-2022 15:06' },
        { title: 'Event 2', description: 'This is event referring to offer  2.', datetime: '01-02-2022 15:06' },
        { title: 'Event 3', description: 'This is event referring to offer  3.', datetime: '01-02-2022 15:06' },
        { title: 'Event 4', description: 'This is event referring to offer  4.', datetime: '01-02-2022 15:06' },
        { title: 'Event 5', description: 'This is event referring to offer  5.', datetime: '01-02-2022 15:06' },
        { title: 'Event 6', description: 'This is event referring to offer  6.', datetime: '01-02-2022 15:06' },
        { title: 'Event 7', description: 'This is event referring to offer  7.', datetime: '01-02-2022 15:06' },
        { title: 'Event 8', description: 'This is event referring to offer  8.', datetime: '01-02-2022 15:06' },
        { title: 'Event 9', description: 'This is event referring to offer  9.', datetime: '01-02-2022 15:06' },
        { title: 'Event 10', description: 'This is event referring to offer  10.', datetime: '01-02-2022 15:06' },
      ],
    };
  },
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
