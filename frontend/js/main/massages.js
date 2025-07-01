const app = Vue.createApp({
    data() {
        return {
            massages: []
        };
    },
    methods: {
        fetchMassages() {
            axios.get("http://localhost:8080/api/reservation/massages")
                .then(response => this.massages = response.data)
                .catch(error => {
                    console.error("Napaka:", error);
                });
        }
    },
    mounted() {
        this.fetchMassages();  // avtomatski priklic ob nalaganju
    }
}).mount("#app");
