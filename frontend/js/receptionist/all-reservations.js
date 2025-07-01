const app = Vue.createApp({
  data() {
    return {
      reservations: [],
      masseuses: [],
      selectedMasseuseId: '',
    };
  },
  computed: {
    filteredReservations() {
      if (!this.selectedMasseuseId) {
        return this.reservations;
      }
      return this.reservations.filter(res => res.masseuse.id == this.selectedMasseuseId);
    }
  },
  methods: {
    fetchReservations() {
      axios.get('http://localhost:8080/api/receptionist/reservations')
        .then(response => {
          console.log("Podatki:", response.data);
          this.reservations = response.data;

          const unique = {};
          this.masseuses = this.reservations
            .map(r => r.masseuse)
            .filter(m => {
              if (!unique[m.id]) {
                unique[m.id] = true;
                return true;
              }
              return false;
            });
        })
        .catch(error => {
          console.error("Napaka:", error);
        });
    },
    editReservation(reservationId) {
      window.location.href = `edit-reservations.html?id=${reservationId}`;
    },
    deleteReservation(reservationId) {
      if (confirm("Ali ste prepričani, da želite izbrisati to rezervacijo?")) {
        axios.delete(`http://localhost:8080/api/receptionist/deletereservation/${reservationId}`)
          .then(response => {
            console.log("Rezervacija izbrisana:", response.data);
            this.fetchReservations();
          })
          .catch(error => {
            console.error("Napaka pri brisanju rezervacije:", error);
          });
      }
    },
    
    formatDateTime(dateTimeString) {
    const dt = new Date(dateTimeString);
    return dt.toLocaleString('sl-SI');},


  },
  mounted() {
    this.fetchReservations();
  }
}).mount('#app');
