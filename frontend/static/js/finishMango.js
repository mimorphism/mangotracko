console.log('Hello World!');

const form = document.querySelector('form'); // grabbing an element on the page
const errorElement = document.querySelector('.error-message');
// const loadingElement = document.querySelector('.loading');
const loadingElement = document.getElementById('progress');
const successElement = document.getElementById('success');
const loadMoreElement = document.querySelector('loadMore');
const API_URL = 'http://localhost:8081'


let skip = 0;
let limit = 5;
let loading = false;
let finished = false;

errorElement.style.display = 'none';
loadingElement.style.display = 'none';
successElement.style.display = 'none';
// document.body.classList.toggle("dark-mode");
// loadMoreElement.style.visibility = 'hidden';


// document.addEventListener('scroll', () => {
//   const rect = loadMoreElement.getBoundingClientRect();
//   if (rect.top < window.innerHeight && !loading && !finished) {
//     loadMore();
//   }
// });


function showSuccessStatus()
{
  loadingElement.style.display = 'none';
  successElement.style.display = '';
}



form.addEventListener('submit', (event) => {
  event.preventDefault();
  const formData = new FormData(form);
  const mangoTitle = formData.get('title');
  const completionDateTime = formData.get('completedDateTime');
  const mangoStatus = formData.get('status');
  const remarks = formData.get('remarks');

  const mango = {
    mangoTitle,
    completionDateTime,
    mangoStatus,
    remarks
  };
  console.log(mango);

  form.style.display = 'none';
  loadingElement.style.display = '';

 
  if (validateMango(mango)) {
    
    axios.post(API_URL + '/completedMango', mango)
    .then(function (response) {
      console.log(response);
      showSuccessStatus();
      setTimeout(resetForm, 2000);
    }).catch(function (error){
      console.log(error);
      loadingElement.style.display = 'none';
      errorElement.style.display = '';
      errorElement.textContent = 'Error saving mango information in DB';
      errorElement.style.display = '';
      // setTimeout(resetForm, 2000);
    });
  } 
  else 
  {
    loadingElement.style.display = 'none';
    errorElement.style.display = '';
    errorElement.textContent = 'All fields must be filled!';
    form.style.display = '';

  }
});

function validateMango(mango) {
  return mango.mangoTitle != "" && mango.completionDateTime != "" && mango.mangoStatus != "" 
  && mango.remarks != "";
}

function resetForm()
{
    errorElement.style.display = 'none';
    loadingElement.style.display = 'none';
    successElement.style.display = 'none';
    form.reset();
    form.style.display = '';
    
}
