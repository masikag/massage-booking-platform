const app = Vue.createApp({
  data() {
    return {
      customers: [],
      search: ""
    };
  },
  methods: {
    getAllCustomers() {
      axios.get('http://localhost:8080/api/customer/receptionist/customers', {
        params: { search: this.search }
      })
      .then(response => {
        this.customers = response.data;
      })
      .catch(error => {
        console.error("Napaka pri pridobivanju strank:", error);
      });
    }
  },
  mounted() {
    this.getAllCustomers();
  }
}).mount('#app');
