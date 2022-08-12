
const form = document.querySelector('form'); // grabbing an element on the page
const errorElement = document.querySelector('.error-message');
// const loadingElement = document.querySelector('.loading');
const loadingElement = document.getElementById('progress');
const successElement = document.getElementById('success');
const loadMoreElement = document.querySelector('loadMore');
var backlogInfo = null;
var mangoTitle = '';


showError(false);
showProgress(false);
showSuccess(false);
initFinishMangoInformation();



function initFinishMangoInformation()
{
  let mangoParam = new URLSearchParams(window.location.search);
  if(mangoParam.get('title') != null)
  {
    mangoTitle = decodeURIComponent(mangoParam.get('title')).trim();
    getBacklogInfo(mangoTitle);
    fillBacklogInfoInForm();
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
  document.getElementById('completedDateTime').value=backlogInfo.lastReadTime;

  }
}



form.addEventListener('submit', (event) => {
  event.preventDefault();
  showForm(false);
  showProgress(true);
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
    
    axios.post('/completedMango', mango)
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
      // setTimeout(resetForm, 2000);
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
};

$(function(){
  $("#mango-header").load("header.html"); 
});


$(function(){
  $("#nav").load("navigation.html"); 
});







