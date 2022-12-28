<template id="diet-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user's Diet profile.</p>
      <p> View <a :href="'/diets'">all users Diet profile</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Diet Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link"
                    @click="updateDiet()">
              <i class="fas fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                    @click="deleteDiet()">
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
            <input type="text" class="form-control" v-model="diet.description" name="description"
                   placeholder="Description" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-food">Food</span>
            </div>
            <input type="text" class="form-control" v-model="diet.food" name="food" placeholder="Food" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-calories">Calories</span>
            </div>
            <input type="number" class="form-control" v-model="diet.calories" name="calories"
                   placeholder="Calories" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-intake">Date</span>
            </div>
            <input type="text" class="form-control" v-model="diet.intake" name="intake"
                   placeholder="Date" />
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-userId">UserId</span>
            </div>
            <input type="number" class="form-control" v-model="diet.userId" name="userId" readonly
                   placeholder="UserId" />
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        {{ diet.description }} with {{ diet.calories }} Calories.
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("diet-profile", {
  template: "#diet-profile",
  data: () => ({
    diet: null,
    noUserFound: false,
  }),
  created: function () {
    const dietId = this.$javalin.pathParams["diet-id"];
    axios.get(`/api/diets/${dietId}`)
        .then(res => this.diet = res.data)
        .catch(error => {
          console.log("No diet found: " + error)
          this.noUserFound = true
        })
  },
  methods: {
    updateDiet: function () {
      const dietId = this.$javalin.pathParams["diet-id"];
      const url = `/api/diets/${dietId}`
      axios.patch(url,
          {
            description: this.diet.description,
            food: this.diet.food,
            calories: this.diet.calories,
            intake: this.diet.intake,
            userId: this.diet.userId
          })
          .then(response =>
              this.diet.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User diet updated!")
    },
    deleteDiet: function () {
      if (confirm("Do you really want to delete?")) {
        const dietId = this.$javalin.pathParams["diet-id"];
        const url = `/api/diets/${dietId}`
        axios.delete(url)
            .then(response => {
              alert("User diet deleted")
              //display the /diets endpoint
              window.location.href = '/diets';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>