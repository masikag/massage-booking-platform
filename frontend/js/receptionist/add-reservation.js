const app = Vue.createApp({
  data() {
    return {
      customerName: "",
      customerSurname: "",
      customerEmail: "",
      customerPhone: "",
      massages: [],
      masseuses: [],
      massageId: null,
      masseuseId: null,
      date: "",
      startTime: "",
      endTime: ""
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios.get("http://localhost:8080/api/reservation/massages")
        .then(res => this.massages = res.data);

      axios.get("http://localhost:8080/api/reservation/masseuses")
        .then(res => this.masseuses = res.data);
    },

    addReservation() {
      if (!this.customerName || !this.customerSurname || !this.customerEmail || !this.customerPhone ||
          !this.massageId || !this.masseuseId || !this.date || !this.startTime || !this.endTime) {
        alert("Prosimo, izpolnite vsa polja!");
        return;
      }

      const startDateTime = `${this.date}T${this.startTime}:00`;
      const endDateTime = `${this.date}T${this.endTime}:00`;

      const request = {
        customerName: this.customerName,
        customerSurname: this.customerSurname,
        customerEmail: this.customerEmail,
        customerPhone: this.customerPhone,
        startTime: startDateTime,
        endTime: endDateTime,
        massageId: this.massageId,
        masseuseId: this.masseuseId
      };

      axios.post("http://localhost:8080/api/receptionist/addreservation", request)
        .then(res => {
          alert(res.data);
          window.location.href = "all-reservations.html";
        })
        .catch(err => {
          console.error("Napaka pri dodajanju rezervacije:", err);
          alert("Napaka pri dodajanju rezervacije!");
        });
    }
  }
});

app.mount("#app");
