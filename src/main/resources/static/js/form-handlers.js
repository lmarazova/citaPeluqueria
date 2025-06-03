// form-handlers.js
// Maneja el envío de formularios con datos generados

document.getElementById('serviceForm').addEventListener('submit', function (e) {
  document.getElementById('serviceJson').value = JSON.stringify(selectedServices);
});

document.getElementById('sendAllForm').addEventListener('submit', function (e) {
  document.getElementById('serviceJsonAll').value = JSON.stringify(selectedServices);
  document.getElementById('blocksJsonAll').value = JSON.stringify(blocks);

  const priceValue = document.getElementById('priceInput').value;
  document.getElementById('priceAll').value = priceValue;

  const packName = document.getElementById('packNameText')?.textContent?.trim();
  if (packName) {
    document.getElementById('packNameAll').value = packName;
  } else {
    alert("Por favor, genera un nombre de pack antes de enviar.");
    e.preventDefault();
  }

  if (!priceValue || priceValue <= 0) {
    alert('Por favor, introduce un precio válido antes de enviar.');
    e.preventDefault();
  }
});

document.getElementById('uploadPhotoForm').addEventListener('submit', function () {
  document.getElementById('serviceJsonForUpload').value = JSON.stringify(selectedServices);
});
