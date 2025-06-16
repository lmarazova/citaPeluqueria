// custom-services.js
// Gestiona los servicios personalizados y su creación
function deleteCustomService(button) {
    const serviceId = button.getAttribute("data-id");

    if (!confirm("¿Seguro que deseas eliminar este servicio personalizado?")) {
      return;
    }

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/delete-custom-service/' + serviceId, {
      method: 'DELETE',
      headers: {
        [csrfHeader]: csrfToken
      }
    }).then(response => {
      if (response.ok) {
        button.closest('li').remove();
      } else {
        alert("Error al eliminar el servicio. Código: " + response.status);
      }
    });
  }
document.addEventListener('DOMContentLoaded', () => {
  const yesBtn = document.getElementById('yesBtn');
  const noBtn = document.getElementById('noBtn');
  const newServiceContainer = document.getElementById('newServiceContainer');
  const addNewServiceBtn = document.getElementById('addNewServiceBtn');
  const newServiceInput = document.getElementById('newServiceInput');
  const servicesList = document.getElementById('servicesList');

  yesBtn.onclick = () => {
    newServiceContainer.style.display = 'block';
    yesBtn.disabled = true;
    noBtn.disabled = true;
  };

  noBtn.onclick = () => {
    newServiceContainer.style.display = 'none';
    yesBtn.disabled = true;
    noBtn.disabled = true;
  };

  addNewServiceBtn.onclick = (e) => {
    e.preventDefault();
    const val = newServiceInput.value.trim();
    if (val.length < 2) {
      alert('Por favor, introduce un nombre válido');
      return;
    }

    const id = 'custom-' + Date.now();

    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';

    const span = document.createElement('span');
    span.innerText = val;

    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = 'btn btn-sm btn-outline-light';
    btn.textContent = '→';
    btn.setAttribute('data-id', id);
    btn.onclick = () => addToPackFromJS(val);

    li.appendChild(span);
    li.appendChild(btn);
    servicesList.appendChild(li);

    e.target.closest('form').submit();
    newServiceInput.value = '';
  };



});
