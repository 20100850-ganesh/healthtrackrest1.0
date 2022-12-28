<template id="medicine-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Medicine Tracker
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add" class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addMedicine">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="description"
                   placeholder="Description" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-medicine">Medicine</span>
            </div>
            <input type="text" class="form-control" v-model="formData.medicine" name="medicine"
                   placeholder="Medicine" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="formData.day" name="day" placeholder="Day" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-intake">Date-Time</span>
            </div>
            <input type="text" class="form-control" v-model="formData.intake" name="intake"
                   placeholder="Date-Time" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number"  class="form-control" v-model="formData.userId" name="userId"
                   placeholder="UserId" />
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addMedicine()">Add
          Medicine</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(medicine1,index) in medicines"
           v-bind:key="index">
        <div class="mr-auto p-2">

          <span><a :href="`/medicines/${medicine1.id}`"> Description:{{ medicine1.description }}, Medicine-Name:({{
              medicine1.medicine}}), Intake-Day:({{medicine1.day}}), Intake-Date:({{medicine1.intake}}), User-Id:({{medicine1.userId}})
                        </a></span>
        </div>
        <div class="p2">
          <a :href="`/medicines/${medicine1.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteMedicine(medicine1, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>
<script>
Vue.component("medicine-overview", {
  template: "#medicine-overview",
  data: () => ({
    users: [],
    medicines: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchUsers();
    this.fetchMedicines();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    fetchMedicines: function () {
      axios.get("/api/medicines")
          .then(res => this.medicines = res.data)
          .catch(() => alert("Error while fetching medicines"));
    },
    deleteMedicine: function (medicine, index) {
      if (confirm('Are you sure you want to delete this medicine? This action cannot be undone.', 'Warning')) {
        //medicine confirmed delete
        const medicineId = medicine.id;
        const url = `/api/medicines/${medicineId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.medicines.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addMedicine: function () {
      const url = `/api/medicines`;
      axios.post(url,
          {
            description: this.formData.description,
            medicine: this.formData.medicine,
            day: this.formData.day,
            intake: this.formData.intake,
            userId: this.formData.userId

          })
          .then(response => {
            this.medicines.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>