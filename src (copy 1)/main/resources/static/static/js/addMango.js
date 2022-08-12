const form = document.querySelector('form'); // grabbing an element on the page
const errorElement = document.querySelector('.error-message');
const loadingElement = document.getElementById('progress');
const successElement = document.getElementById('success');
var updateBacklog = false;
var mangoTitle = '';
var backlogInfo = null;


showError(false);
showProgress(false);
showSuccess(false);


getUpdateBacklogStatus();

if(updateBacklog)
{
  fillBacklogInfoInForm();
}

///////////////////////////////////////////////////////////////////////
function showSuccess(visible)
{
  if(visible)
  {
    successElement.style.display = '';
  }else
  {
    successElement.style.display = 'none';
  }
}

function showProgress(visible)
{
  if(visible)
  {
    loadingElement.style.display = '';
  }
  else
  {
    loadingElement.style.display = 'none';
  }
}

function showError(visible)
{
  if(visible)
  {
    errorElement.style.display = '';
  }
  else
  {
    errorElement.style.display = 'none';
  }
}

function showForm(visible)
{
  if(visible)
  {
    form.style.display = '';
  }
  else
  {
    form.style.display = 'none';
  }
}



form.addEventListener('submit', (e) => {
  e.preventDefault();
  showForm(false);
  showProgress(true);
  const formData = new FormData(form);
  const mangoTitle = formData.get('title');
  const lastChapterRead = formData.get('lastChapterRead');
  const lastReadTime = formData.get('lastChapterReadTime');

  const mango = {
    mangoTitle,
    lastChapterRead,
    lastReadTime
  };

  if(updateBacklog)
  {
    updateMango(mango);
  }else{
    newMango(mango);
  }

}, false);

function validateMango(mango) {
  return mango.mangoTitle != "" && mango.lastChapterRead != "" && mango.lastReadTime != "";
}

function resetForm()
{
    showError(false);
    showProgress(false);
    showSuccess(false);
    form.reset();
    showForm(true);
}

function newMango(mango)
{
  if (validateMango(mango)) {
    axios.post('/newMango', mango)
    .then(function (response) {
      console.log(response);
      showProgress(false);
      showSuccess(true);
      setTimeout(resetForm, 2000);
    }).catch(function (error){
      showProgress(false)
      console.log(error);
      showError(true);
      errorElement.textContent = 'Error saving mango information in DB';
      setTimeout(resetForm, 2000);
    });
  } 
  else 
  {
    showProgress(false);
    showForm(true);
    showError(true);
    errorElement.textContent = 'All fields must be filled!';
    setTimeout(function(){ 
      resetForm();
    }, 2000);

  }
}

function updateMango(mango)
{
  if (validateMango(mango)) {
    axios.put('/updateBacklog', mango)
    .then(function (response) {
      console.log(response);
      showProgress(false);
      showSuccess(true);
      setTimeout(resetForm, 2000);

    }).catch(function (error){
      console.log(error);
      showProgress(false);
      showError(true);
      errorElement.textContent = 'Error saving mango information in DB';
      setTimeout(resetForm, 2000);

    });
  } 
  else 
  {
    showProgress(false);
    showForm(true);
    showError(true);
    errorElement.textContent = 'All fields must be filled!';
    setTimeout(resetForm, 2000);

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


$(function(){
  $("#mango-header").load("header.html"); 
});


$(function(){
  $("#nav").load("navigation.html"); 
});


