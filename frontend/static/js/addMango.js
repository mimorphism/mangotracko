console.log('Hello World!');

const form = document.querySelector('form'); // grabbing an element on the page
const errorElement = document.querySelector('.error-message');
// const loadingElement = document.querySelector('.loading');
const loadingElement = document.getElementById('progress');
const successElement = document.getElementById('success');
const loadMoreElement = document.querySelector('loadMore');
const API_URL = 'http://localhost:8081';
var updateBacklog = false;
var mangoTitle = '';
var backlogInfo = null;


let skip = 0;
let limit = 5;
let loading = false;
let finished = false;

errorElement.style.display = 'none';
loadingElement.style.display = 'none';
successElement.style.display = 'none';

getUpdateBacklogStatus();

if(updateBacklog)
{
  fillBacklogInfoInForm();
}

function showSuccessStatus()
{
  loadingElement.style.display = 'none';
  successElement.style.display = '';
}



form.addEventListener('submit', (event) => {
  event.preventDefault();
  const formData = new FormData(form);
  const mangoTitle = formData.get('title');
  const lastChapterRead = formData.get('lastChapterRead');
  const lastReadTime = formData.get('lastChapterReadTime');

  const mango = {
    mangoTitle,
    lastChapterRead,
    lastReadTime
  };
  console.log(mango);
  if(updateBacklog)
  {
    updateMango(mango);
  }else{
    newMango(mango);
  }

  form.style.display = 'none';
  loadingElement.style.display = '';

 
 
});

function validateMango(mango) {
  return mango.mangoTitle != "" && mango.lastChapterRead != "" && mango.lastReadTime != "";
}

function resetForm()
{
    errorElement.style.display = 'none';
    loadingElement.style.display = 'none';
    successElement.style.display = 'none';
    form.reset();
    form.style.display = '';
    
}

function newMango(mango)
{
  if (validateMango(mango)) {
    
    axios.post(API_URL + '/newMango', mango)
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
      setTimeout(resetForm, 2000);
    });
  } 
  else 
  {
    loadingElement.style.display = 'none';
    errorElement.style.display = '';
    errorElement.textContent = 'All fields must be filled!';
    form.style.display = '';

  }
}

function updateMango(mango)
{
  if (validateMango(mango)) {
    
    axios.put(API_URL + '/updateBacklog', mango)
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
      setTimeout(resetForm, 2000);
    });
  } 
  else 
  {
    loadingElement.style.display = 'none';
    errorElement.style.display = '';
    errorElement.textContent = 'All fields must be filled!';
    form.style.display = '';

  }
}

function getUpdateBacklogStatus(mangoTitle)
{
  let mangoParam = new URLSearchParams(window.location.search);
  if(mangoParam.get('title') != null)
  {
    updateBacklog = true;
    mangoTitle = decodeURIComponent(mangoParam.get('title')).trim();
    getBacklogInfo(mangoTitle);
  }
  else
  {
    return false;
  }

}

function getBacklogInfo(mangoTitle)
{
  backlogInfo = JSON.parse(localStorage.getItem(mangoTitle));
  localStorage.clear();

}

function fillBacklogInfoInForm(){
  if(backlogInfo != null){
    document.getElementById('title').value=backlogInfo.mangoTitle;
  document.getElementById('lastChapterRead').value=backlogInfo.lastChapterRead;
  document.getElementById('lastChapterReadTime').value=backlogInfo.lastReadTime;

  }
  
}