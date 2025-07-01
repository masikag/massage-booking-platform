const app = Vue.createApp({
    data() {
        return {
            customerName: "",
            customerSurname: "",
            customerEmail: "",
            customerPhone: "",
            password: "",
            checkPassword: "",
            errorMessage: ""
        };
    },
    methods: {
        registerCustomer() {
        if (!this.customerName || !this.customerSurname || !this.customerEmail || !this.customerPhone || !this.password || !this.checkPassword) {
            this.errorMessage = "Prosimo, izpolnite vsa polja!";
            return;
        }

        if (this.password !== this.checkPassword) {
            this.errorMessage = "Gesli se ne ujemata.";
            return;
        }

        const request = {
            name: this.customerName,
            surname: this.customerSurname,
            email: this.customerEmail,
            phone: this.customerPhone,
            password: this.password
        };

        axios.post("http://localhost:8080/api/customer/register", request)
            .then(response => {
            if (response.data === "Registracija uspešna") {
                alert("Registracija uspešna!")
                window.location.href = "login.html";
            } else {
                this.errorMessage = response.data;
            }
            })
            .catch(error => {
            console.error(error);
            this.errorMessage = "Napaka pri povezavi s strežnikom.";
            });
        }

    }
}).mount("#app");
