const { createApp } = Vue;

createApp({
    data() {
        return {
            massages: [],
            filterDuration: "",
            filterPrice: 100, 
            selectedMassage: null,
            masseuses: [],
            availableSlots: [],
            availableSlotsAll: [],
            selectedMassage: null,
            selectedMasseuseId: "",
            selectedMasseuseId: null,
            selectedDate: null,
            selectedTime: null,
            email: "",
            password: "",
            confirmationMessage: "",
            filterDuration: "",
            filterPrice: ""
        };
    },
    mounted() {
        this.fetchAllMassages();
        this.fetchAllMasseuses();
    },
    methods: {
        fetchAllMassages() {
            axios.get("http://localhost:8080/api/reservation/massages")
            .then(res => this.massages = res.data);
        },
        fetchFilteredMassages() {
            let url = "http://localhost:8080/api/reservation/massages";
            if (this.filterDuration || this.filterPrice) {
            url = `http://localhost:8080/api/reservation/filter?duration=${this.filterDuration}&price=${this.filterPrice}`;
            }
            axios.get(url)
            .then(res => this.massages = res.data);
        },
        fetchAllMasseuses() {
            axios.get("http://localhost:8080/api/reservation/masseuses")
            .then(res => this.masseuses = res.data);
        },
        selectMassage(massage) {
            this.selectedMassage = massage;
            this.availableSlots = [];
            this.selectedMasseuseId = null;
            this.selectedDate = null;
            this.selectedTime = null;
        },

        fetchSlots() {
            if (!this.selectedDate || !this.selectedMassage) return; // obvezni!

            const duration = this.selectedMassage.duration;

            // izbrana maserka
            if (this.selectedMasseuseId) {
                axios.get("http://localhost:8080/api/reservation/slots", {
                params: {
                    masseuseId: this.selectedMasseuseId,
                    date: this.selectedDate,
                    durationMinutes: duration
                }
                })
                .then(response => {
                this.availableSlots = response.data;
                })
                .catch(error => {
                console.error("Napaka pri pridobivanju terminov:", error);
                });

            } else {
                // brez preference maserke
                axios.get("http://localhost:8080/api/reservation/slots/all", {
                params: {
                    date: this.selectedDate,
                    durationMinutes: duration
                }
                })
                .then(response => {
                this.availableSlotsAll = response.data;
                })
                .catch(error => {
                console.error("Napaka pri pridobivanju vseh terminov:", error);
                });
            }
        },
        confirmReservation() {
            axios.post("http://localhost:8080/api/reservation/confirm", {
            email: this.email,
            password: this.password,
            massageId: this.selectedMassage.id,
            masseuseId: this.selectedMasseuseId,
            date: this.selectedDate,
            time: this.selectedTime
        })
        .then(res => {
            this.confirmationMessage = res.data;
        })
        .catch(err => {
            this.confirmationMessage = "Napaka pri potrditvi.";
            console.error(err);
        });
        },
        
        findMasseuseIdByName(name) {
        const found = this.masseuses.find(m => m.masseuseName === name);
        return found ? found.id : null;
        }
    }
}).mount("#app");
