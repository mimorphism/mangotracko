console.log('Hello World!');

const form = document.querySelector('form'); // grabbing an element on the page
const errorElement = document.querySelector('.error-message');
const loadingElement = document.querySelector('.loading');
const mewsElement = document.querySelector('.mews');
const loadMoreElement = document.querySelector('loadMore');
const API_URL = 'https://localhost:sumport'
var header = document.querySelector('.header');


let skip = 0;
let limit = 5;
let loading = false;
let finished = false;

errorElement.style.display = 'none';
// document.body.classList.toggle("dark-mode");
loadMoreElement.style.visibility = 'hidden';



window.addEventListener('scroll', function(){
  // var header = document.querySelector('.test');
  header.classList.toggle('sticky', window.scrollY > 0);
});



form.addEventListener('submit', (event) => {
  event.preventDefault();
  const formData = new FormData(form);
  const name = formData.get('name');
  const content = formData.get('content');

  if (name.trim() && content.trim()) {
    errorElement.style.display = 'none';
    form.style.display = 'none';
    loadingElement.style.display = 'none';

    const mango = {
      title,
      content
    };

  } else {
    errorElement.textContent = 'Name and content are required!';
    errorElement.style.display = '';
  }
});



