const app = Vue.createApp({
    data() {
        return {
        customer: null,
        customerReservations: [],
        customerId: localStorage.getItem("customerId")
        };
    },
    methods: {
        fetchCustomerData() {
            axios.get(`http://localhost:8080/api/customer/${this.customerId}`)
                .then(res => {
                this.customer = res.data;
                });

            axios.get(`http://localhost:8080/api/reservation/my/${this.customerId}`)
                .then(res => {
                this.customerReservations = res.data;
                });
        },

        formatDate(dateTime) {
        return new Date(dateTime).toLocaleDateString("sl-SI");
        },
        formatTime(dateTime) {
        return new Date(dateTime).toLocaleTimeString("sl-SI", { hour: '2-digit', minute: '2-digit' });
        },

        editReservation(reservationId) {
        window.location.href = `edit-reservation.html?id=${reservationId}`;
        },
        
        deleteReservation(reservationId) {
        if (!confirm("Ali ste prepričani, da želite izbrisati rezervacijo?")) return;

        axios.post(`http://localhost:8080/api/reservation/delete`, {
            reservationId: reservationId,
            customerId: this.customerId
        })
        .then(res => {
            alert(res.data);
            this.fetchCustomerData();
        })
        .catch(err => {
            console.error(err);
            alert("Napaka pri brisanju rezervacije.");
        });
        }
    },
    mounted() {
        if (!this.customerId) {
        alert("Niste prijavljeni.");
        window.location.href = "login.html";
        } else {
        this.fetchCustomerData();
        }
    }
});
app.mount("#app");