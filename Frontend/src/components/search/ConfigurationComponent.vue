<template>
  <div class="search-box">
    <h1>Configuration</h1>
    <form @submit.prevent="submitConfig">
      <!-- Persons Range -->
      <label for="persons">Persons: {{ persons }}</label>
      <input type="range" id="persons" min="1" max="6" v-model.number="persons" @input="validateKids">

      <!-- Kids Range -->
      <label for="kids">Kids: {{ kids }}</label>
      <input type="range" id="kids" :max="maxKids" v-model.number="kids">

      <!-- Kids Ages -->
      <div v-for="(kid, index) in kids" :key="'kid-age-' + index">
        <label :for="'kid-age-' + index">Kid {{ index + 1 }} age:</label>
        <input type="date" :id="'kid-age-' + index" v-model="kidAges[index]">
      </div>

      <!-- Submit Button -->
      <button type="submit">Update</button>
    </form>
  </div>
</template>


<script>

export default {
  components: {

  },
  mounted() {

  },
  data() {
    return {
      persons: 1,
      kids: 0,
      kidAges: []
    };
  },
  watch: {
    kids(newVal) {
      this.kidAges = Array(newVal).fill(''); // Reset kidAges when number of kids changes
    },
    kidAges() {
      this.emitPersons();
    }
  },
  computed: {
    maxKids() {
      return Math.max(0, this.persons - 1);
    }
  },
  methods: {
    validateKids() {
      if (this.kids >= this.persons) {
        this.kids = this.maxKids;
      }
    },
    emitPersons() {
      this.$emit('update-persons', {
        persons: this.persons,
        kids: this.kidAges
      });
    },
    submitConfig() {

      const params = {
        persons: this.persons,
        kids: this.kidAges
      };
      console.log(params)
      this.emitPersons();

      // Filter out empty parameters
      const searchParams = {};
      Object.keys(params).forEach(key => {
        if (params[key]) searchParams[key] = params[key];
      });

      console.log(searchParams)

      this.$router.push({ name: '', query: searchParams }).catch(err => {
        // Handle duplicate navigation errors or any other router error
        if (err.name !== 'NavigationDuplicated' && err.message !== 'Avoided redundant navigation to current location') {
          console.error(err);
        }
      });
    },
  },
};
</script>
<style scoped>
.search-box {
  padding: 20px;
  background-color: #f7f7f7;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.search-box form {
  display: flex;
  flex-direction: column;
}

label {
  color: #35495e;
  margin: 10px 0 5px;
}

input[type="range"],
input[type="date"] { /* Apply the existing styles also to date inputs */
  -webkit-appearance: none;
  height: 8px;
  border-radius: 5px;
  background: #42b883;
  outline: none;
  padding: 0;
  margin: 0 0 20px;
}

input[type="date"] { /* Additional styling specific to date inputs */
  height: auto; /* Resets the height to default for date inputs */
  padding: 10px; /* Matches the padding of your select elements */
  border: 1px solid #ccc; /* Matches the border of select elements */
  border-radius: 5px; /* Matches the border-radius of select elements */
  background-color: white; /* Ensures the background color matches */
}

input[type="range"]::-webkit-slider-thumb,
input[type="date"]::-webkit-calendar-picker-indicator { /* Style adjustments for webkit browsers */
  -webkit-appearance: none;
  margin: 0; /* Specific adjustments might be needed depending on the browser */
}

input[type="range"]::-webkit-slider-thumb {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: #35495e;
  cursor: pointer;
}

/* You might not need to adjust the following for date pickers, but ensure consistency in design */
input[type="range"]::-moz-range-thumb {
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: #35495e;
  cursor: pointer;
}

select,
input[type="date"] { /* Ensure select and date inputs share styles */
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: white;
}


button {
  background-color: #42b883;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;

  margin: 10px 0 5px;
}

button:hover {
  background-color: #35495e;
}
</style>
