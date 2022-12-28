<template id="diet-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Diets
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
        <form id="addDiet">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-description">Description</span>
            </div>
            <input type="text" class="form-control" v-model="formData.description" name="description"
                   placeholder="Description" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-food">Food</span>
            </div>
            <input type="text" class="form-control" v-model="formData.food" name="food"
                   placeholder="Food" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-calories">Calories</span>
            </div>
            <input type="number"  class="form-control" v-model="formData.calories" name="calories"
                   placeholder="Calories" />
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
                   placeholder="userId" />
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addDiet()">Add
          Diet</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start" v-for="(diet,index) in diets" v-bind:key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/diets/${diet.id}`"> <span class="text-success">Description:</span>{{ diet.description }}, <span class="text-success">Food-Name:</span>({{
              diet.food}}), <span class="text-success">Calories:</span>({{diet.calories}}), <span class="text-success">Intake-Time:</span>({{diet.intake}}), <span class="text-success">UserId:</span>({{diet.userId}})
                        </a></span>
        </div>
        <div class="p2">
          <a :href="`/diets/${diet.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteDiet(diet, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>
<script>
Vue.component("diet-overview", {
  template: "#diet-overview",
  data: () => ({
    users: [],
    diets: [],
    formData: [],
    hideForm: true,
  }),
  created() {
    this.fetchUsers();
    this.fetchDiets();
  },
  methods: {
    fetchUsers: function () {
      axios.get("/api/users")
          .then(res => this.users = res.data)
          .catch(() => alert("Error while fetching users"));
    },
    fetchDiets: function () {
      axios.get("/api/diets")
          .then(res => this.diets = res.data)
          .catch(() => alert("Error while fetching diet record"));
    },
    deleteDiet: function (diet, index) {
      if (confirm('Are you sure you want to delete this diet record? This action cannot be undone.', 'Warning')) {
        //diet confirmed delete
        const dietId = diet.id;
        const url = `/api/diets/${dietId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.diets.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addDiet: function () {
      const url = `/api/diets`;
      axios.post(url,
          {
            description: this.formData.description,
            food: this.formData.food,
            calories: this.formData.calories,
            intake: this.formData.intake,
            userId: this.formData.userId

          })
          .then(response => {
            this.diets.push(response.data)
            this.hideForm = true;
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>