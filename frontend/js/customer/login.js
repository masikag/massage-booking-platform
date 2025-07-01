const app = Vue.createApp({
  data() {
    return {
      email: "",
      password: "",
      customerId: null,
      errorMessage: ""
    };
  },
  methods: {
    loginCustomer() {
      axios.get(`http://localhost:8080/api/customer/login?email=${this.email}&password=${this.password}`)
        .then(res => {
          const data = res.data;
          console.log(data);

          if (data.message === "Prijava uspešna") {
            if (data.customerId) {
              localStorage.setItem("customerId", data.customerId);
              window.location.href = "my-reservations.html";
            }
          } else {
            this.errorMessage = "Napačno uporabniško ime ali geslo.";
          }
        })
        .catch(err => {
          console.error(err);
          this.errorMessage = "Napaka pri povezavi s strežnikom.";
        });
    }
  }
}).mount("#app");
