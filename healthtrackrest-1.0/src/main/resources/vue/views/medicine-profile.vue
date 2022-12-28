<template id="medicine-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user's medicine profile.</p>
      <p> View <a :href="'/medicines'">all users medicine profile</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Medicine Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link"
                    @click="updateMedicine()">
              <i class="fas fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteMedicine()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="medicine1.description" name="description"
                   placeholder="Description" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-medicine">Medicine</span>
            </div>
            <input type="text" class="form-control" v-model="medicine1.medicine" name="medicine"
                   placeholder="Medicine" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="medicine1.day" name="day" placeholder="Day" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-intake">Date</span>
            </div>
            <input type="text" class="form-control" v-model="medicine1.intake" name="intake"
                   placeholder="Date" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number" class="form-control" v-model="medicine1.userId" name="userId" readonly
                   placeholder="UserId" />
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        {{ medicine1.description }} - {{medicine1.medicine}} on {{ medicine1.day }}.
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("medicine-profile", {
  template: "#medicine-profile",
  data: () => ({
    medicine1: null,
    noUserFound: false,
  }),
  created: function () {
    const medicineId = this.$javalin.pathParams["medicine-id"];
    axios.get(`/api/medicines/${medicineId}`)
        .then(res => this.medicine1 = res.data)
        .catch(error => {
          console.log("No medicine found: " + error)
          this.noUserFound = true
        })
  },
  methods: {
    updateMedicine: function () {
      const medicineId = this.$javalin.pathParams["medicine-id"];
      const url = `/api/medicines/${medicineId}`
      axios.patch(url,
          {
            description: this.medicine1.description,
            medicine: this.medicine1.medicine,
            day: this.medicine1.day,
            intake: this.medicine1.intake,
            userId: this.medicine1.userId
          })
          .then(response =>
              this.medicine1.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User medicine updated!")
    },
    deleteMedicine: function () {
      if (confirm("Do you really want to delete?")) {
        const medicineId = this.$javalin.pathParams["medicine-id"];
        const url = `/api/medicines/${medicineId}`
        axios.delete(url)
            .then(response => {
              alert("User medicine deleted")
              //display the /medicines endpoint
              window.location.href = '/medicines';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>