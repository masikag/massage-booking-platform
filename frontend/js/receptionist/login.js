const app = Vue.createApp({
  data() {
    return {
      username: "",
      password: "",
      errorMessage: ""
    }
  },
  methods: {
    async login() {
      try {
        const response = await axios.post("http://localhost:8080/api/receptionist/login", {
          username: this.username,
          password: this.password
        });

        if (response.data === "Prijava uspešna!") {
          window.location.href = "dashboard.html";
        } else {
          this.errorMessage = "Napačno uporabniško ime ali geslo.";
        }
      } catch (error) {
        this.errorMessage = "Prišlo je do napake pri prijavi.";
        console.error(error);
      }
    }
  }
});

app.mount("#app");
