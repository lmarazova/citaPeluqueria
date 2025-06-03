// blocks.js
// Gestiona los bloques de tiempo (activos / pasivos)

let blocks = [];

function addBlock() {
  blocks.push({ type: "active" });
  render();
}

function toggleType(index) {
  blocks[index].type = blocks[index].type === "active" ? "passive" : "active";
  render();
}

function deleteBlock(index) {
  blocks.splice(index, 1);
  render();
}

function render() {
  const container = document.getElementById("blocks");
  container.innerHTML = "";

  let actives = 0;
  let passives = 0;

  blocks.forEach((block, index) => {
    const div = document.createElement("div");
    div.className = "block " + block.type;
    div.style.position = "relative";

    const text = document.createElement("span");
    text.title = block.type === "active" ? "Manipulado (active)" : "Esperando (passive)";
    text.style.cursor = "pointer";
    text.onclick = () => toggleType(index);
    text.innerHTML = block.type === "active"
      ? '<i class="fa-solid fa-scissors"></i>'
      : '<i class="fa-solid fa-hourglass-half"></i>';

    const btnDelete = document.createElement("button");
    btnDelete.innerText = "✖";
    btnDelete.title = "Eliminar bloque";
    btnDelete.style = `
      position: absolute;
      top: 2px;
      right: 2px;
      background: transparent;
      border: none;
      color: red;
      cursor: pointer;
      font-size: 14px;
    `;
    btnDelete.onclick = (e) => {
      e.stopPropagation();
      deleteBlock(index);
    };

    div.appendChild(text);
    div.appendChild(btnDelete);
    container.appendChild(div);

    if (block.type === "active") actives++;
    else passives++;
  });

  const total = blocks.length * 15;
  document.getElementById("resume").innerText =
    `Total: ${total} min | Activos: ${actives * 15} min | Pasivos: ${passives * 15} min`;
}

document.getElementById("blocksForm").addEventListener("submit", function (e) {
  const input = document.getElementById("blocksJson");
  input.value = JSON.stringify(blocks);
});
