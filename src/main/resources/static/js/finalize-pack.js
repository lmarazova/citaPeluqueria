// finalize-pack.js
// Gestiona el botón "Listo" y genera nombre del pack visual

document.addEventListener('DOMContentLoaded', () => {
  const finalizeButton = document.getElementById('finalizeButton');
  const selectedServicesList = document.getElementById('selectedServicesList');
  const packNameText = document.getElementById('packNameText');

  finalizeButton.addEventListener('click', () => {
    const selectedLabels = Array.from(selectedServicesList.querySelectorAll('li'))
      .map(li => li.textContent.replace('❌', '').trim());

    const packName = selectedLabels.join(' + ');
    packNameText.textContent = packName;

    selectedServicesList.style.display = 'none';
    finalizeButton.style.display = 'none';
  });
});
