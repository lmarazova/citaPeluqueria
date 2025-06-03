// service-pack.js
// Lógica de añadir servicios al pack (predefinidos o personalizados)

const selectedServices = [];
const packPreview = document.getElementById('packPreview');

function addToPack(button) {
  const serviceId = button.getAttribute('data-id');
  const serviceLabel = button.closest('li').querySelector('span').innerText;

  if (selectedServices.some(s => s.id === serviceId)) {
    alert("Servicio ya añadido.");
    return;
  }

  selectedServices.push({ id: serviceId, label: serviceLabel });
  renderSelectedServices();
}

function addToPackFromJS(name) {
  const li = document.createElement('li');
  li.className = 'list-group-item';
  li.textContent = name;
  packPreview.appendChild(li);
}

function renderSelectedServices() {
  const list = document.getElementById('selectedServicesList');
  list.innerHTML = '';

  selectedServices.forEach(service => {
    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';
    li.textContent = service.label;

    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = 'btn btn-sm btn-outline-danger';
    btn.textContent = '❌';
    btn.onclick = () => removeService(service.id);

    li.appendChild(btn);
    list.appendChild(li);
  });
}

function removeService(serviceId) {
  const index = selectedServices.findIndex(s => s.id === serviceId);
  if (index !== -1) {
    selectedServices.splice(index, 1);
    renderSelectedServices();
  }
}
