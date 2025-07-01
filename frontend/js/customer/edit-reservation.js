const app = Vue.createApp({
    data() {
        return {
            customer: {},
            customerId: null,
            reservation: {},
            massageName: "",
            masseuseName: "",
            date: "",
            startTime: "",
            endTime: "",
            masseuses: [], 
            massages: [], 
            reservationId: null
        };
    },
    mounted() {
        const urlParams = new URLSearchParams(window.location.search);
        this.reservationId = urlParams.get("id");
        if (this.reservationId) {
            this.fetchReservation();
        }
    },
    methods: {
        fetchReservation() {
            axios.get(`http://localhost:8080/api/reservation/${this.reservationId}`)
                .then(response => {
                    this.reservation = response.data;

                    // nastavimo podatke iz rezervacije -> pretvorimo datetime v date in time format
                    if (this.reservation.date) {
                        // ce je datum v ISO formatu, ga pretvorimo
                        const dateObj = new Date(this.reservation.date);
                        this.date = dateObj.toISOString().split('T')[0]; //YYYY-MM-DD
                    }

                    // pretvorimo cas v HH:MM format
                    if (this.reservation.startTime) {
                        this.startTime = this.formatTimeForInput(this.reservation.startTime);
                    }
                    if (this.reservation.endTime) {
                        this.endTime = this.formatTimeForInput(this.reservation.endTime);
                    }

                    this.loadRelatedData();
                })
                .catch(error => {
                    console.error('Napaka pri nalaganju rezervacije:', error);
                    alert('Napaka pri nalaganju rezervacije!');
                });
        },
        loadRelatedData() {
            axios.get("http://localhost:8080/api/reservation/massages")
                .then(response => {
                    this.massages = response.data;
                    // nastavimo trenutno izbrano masažo
                    const currentMassage = this.massages.find(m => m.id === this.reservation.massageId);
                    if (currentMassage) {
                        this.massageName = currentMassage.massageName;
                    }
                })
                .catch(error => {
                    console.error('Napaka pri nalaganju masaž:', error);
                });

            axios.get("http://localhost:8080/api/reservation/masseuses")
                .then(response => {
                    this.masseuses = response.data;

                    const currentMasseuse = this.masseuses.find(m => m.id === this.reservation.masseuseId);
                    if (currentMasseuse) {
                        this.masseuseName = currentMasseuse.masseuseName;
                    }
                })
                .catch(error => {
                    console.error('Napaka pri nalaganju maserk:', error);
                });

            // podatki o stranki
            if (this.reservation.customerId) {
                axios.get(`http://localhost:8080/api/customer/${this.reservation.customerId}`)
                    .then(response => {
                        this.customer = response.data;
                    })
                    .catch(error => {
                        console.error('Napaka pri nalaganju podatkov o stranki:', error);
                    });
            }
        },
        editReservation() {
            if (!this.date || !this.startTime || !this.endTime || !this.massageName || !this.masseuseName) {
                alert('Prosimo izpolnite vsa polja!');
                return;
            }

            const selectedMassage = this.massages.find(m => m.massageName === this.massageName);
            const selectedMasseuse = this.masseuses.find(m => m.masseuseName === this.masseuseName);

            if (!selectedMassage || !selectedMasseuse) {
                alert('Prosimo izberite masažo in maserko!');
                return;
            }

            if (this.endTime <= this.startTime) {
                alert("Končni čas mora biti po začetnem času.");
                return;
            }

            const openingTime = "10:00";
            const closingTime = "20:00";

            if (this.startTime < openingTime || this.endTime > closingTime) {
                alert("Izbran termin je izven delovnega časa (10:00–20:00)!");
                return;
            }

            const dto = {
                customerId: this.reservation.customerId,
                reservationId: parseInt(this.reservationId),
                startTime: `${this.date}T${this.startTime}:00`,
                endTime: `${this.date}T${this.endTime}:00`,
                masseuseId: selectedMasseuse.id,
                massageId: selectedMassage.id
            };

            axios.put(`http://localhost:8080/api/reservation/edit/${this.reservationId}`, dto)
                .then(response => {
                    alert("Rezervacija uspešno posodobljena!");
                    window.location.href = 'my-reservations.html';
                })
                .catch(error => {
                    console.error('Napaka pri posodabljanju rezervacije:', error);
                    if (error.response && error.response.data) {
                        alert(`Napaka: ${error.response.data.message || 'Neznana napaka'}`);
                    } else {
                        alert('Napaka pri posodabljanju rezervacije!');
                    }
                });
        },

        formatTimeForInput(timeString) {
            // ce je v formatu "HH:MM:SS" ali "HH:MM" -> vrnemo samo "HH:MM"
            if (typeof timeString === 'string') {
                return timeString.substring(0, 5); // vzamemo samo HH:MM
            }
            return timeString;
        },

        formatTimeForServer(timeString) {
            // v primeru -> format "HH:MM:SS" -> dodamo sekunde
            if (timeString && timeString.length === 5) {
                return timeString + ':00';
            }
            return timeString;
        }
    }

}).mount("#app");