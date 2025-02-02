// 전역 변수: API에서 받아온 메뉴 데이터 및 현재 선택된 메뉴/옵션
let menus = [];
let selectedMenu = null;
let selectedTemperature = null;
let selectedSize = null;



// [메뉴리스트] 버튼 클릭 시 API 호출 및 모달 활성화
document.getElementById('menu-list').addEventListener('click', function() {
    const corpBrand = getCorpBrand();
    fetch(`/api/v1/menus/${corpBrand}`)
        .then(response => response.json())
        .then(data => {
            menus = data;
            console.log(menus);
            renderCategories();
            // 기본으로 "커피" 카테고리 선택 (해당 데이터가 있으면)
            const defaultCategory = "커피";
            if(menus.some(menu => menu.category_name === defaultCategory)) {
                loadMenuItems(defaultCategory);
            } else if(menus.length > 0) {
                // "커피"가 없으면 첫번째 카테고리 선택
                loadMenuItems(menus[0].category_name);
            }
            openMenuModal();
        })
        .catch(error => console.error('메뉴 데이터를 불러오는 중 오류:', error));
});

// 카테고리 버튼들을 생성하는 함수
function renderCategories() {
    const categorySet = new Set();
    menus.forEach(menu => categorySet.add(menu.category_name));
    const categories = Array.from(categorySet);
    const container = document.getElementById('category-buttons');
    container.innerHTML = '';
    categories.forEach(category => {
        const btn = document.createElement('button');
        btn.textContent = category;
        btn.className = "px-4 py-2 rounded border hover:bg-gray-100";
        btn.addEventListener('click', () => loadMenuItems(category));
        container.appendChild(btn);
    });
}

// 선택한 카테고리에 속하는 메뉴 항목들을 grid에 렌더링
function loadMenuItems(category) {
    const grid = document.getElementById('menu-grid');
    grid.innerHTML = '';
    const filteredMenus = menus.filter(menu => menu.category_name === category);
    filteredMenus.forEach(menu => {
        const div = document.createElement('div');
        div.className = "cursor-pointer border rounded overflow-hidden hover:shadow-lg transition-shadow";
        div.innerHTML = `
          <img src="${menu.image_url}" alt="${menu.name}" class="w-full h-32 object-cover">
          <div class="p-2 text-center font-medium">${menu.name}</div>
        `;
        div.addEventListener('click', () => openDetailModal(menu));
        grid.appendChild(div);
    });
}

// 모달 열기/닫기 함수
function openMenuModal() {
    const modal = document.getElementById('menuModal');
    modal.classList.remove('hidden');
    modal.classList.add('flex');
}
function closeMenuModal() {
    const modal = document.getElementById('menuModal');
    modal.classList.add('hidden');
    modal.classList.remove('flex');
}
function openDetailModal(menu) {
    selectedMenu = menu;
    selectedTemperature = null;
    selectedSize = null;

    // 상세 모달 이미지 설정
    document.getElementById('detailImage').src = menu.image_url;

    // 온도 옵션 생성 (temperatures가 존재할 경우)
    const tempContainer = document.getElementById('temperature-options');
    const tempOptionsDiv = tempContainer.querySelector('.flex');
    if(menu.temperatures && menu.temperatures.length > 0) {
        tempContainer.classList.remove('hidden');
        tempOptionsDiv.innerHTML = '';
        menu.temperatures.forEach(temp => {
            const label = document.createElement('label');
            label.className = "inline-flex items-center";
            label.innerHTML = `
            <input type="radio" name="temperature" value="${temp}" class="form-radio">
            <span class="ml-2">${temp}</span>
          `;
            label.querySelector('input').addEventListener('change', function() {
                selectedTemperature = this.value;
            });
            tempOptionsDiv.appendChild(label);
        });
    } else {
        tempContainer.classList.add('hidden');
    }

    // 사이즈 옵션 생성 (menu_sizes가 존재할 경우)
    const sizeContainer = document.getElementById('size-options');
    const sizeOptionsDiv = sizeContainer.querySelector('.flex');
    if(menu.menu_sizes && menu.menu_sizes.length > 0) {
        sizeContainer.classList.remove('hidden');
        sizeOptionsDiv.innerHTML = '';
        menu.menu_sizes.forEach(size => {
            const label = document.createElement('label');
            label.className = "inline-flex items-center";
            label.innerHTML = `
            <input type="radio" name="menu_size" value="${size}" class="form-radio">
            <span class="ml-2">${size}</span>
          `;
            label.querySelector('input').addEventListener('change', function() {
                selectedSize = this.value;
            });
            sizeOptionsDiv.appendChild(label);
        });
    } else {
        sizeContainer.classList.add('hidden');
    }

    // 상세 선택 모달 열기
    const detailModal = document.getElementById('detailModal');
    detailModal.classList.remove('hidden');
    detailModal.classList.add('flex');
}
function closeDetailModal() {
    const detailModal = document.getElementById('detailModal');
    detailModal.classList.add('hidden');
    detailModal.classList.remove('flex');
}

// [선택완료] 버튼 클릭 시, 최종 메뉴명을 구성하고 모달들을 닫음.
document.getElementById('confirmSelection').addEventListener('click', function() {
    if(selectedMenu) {
        let finalName = "";
        const hasTemp = selectedMenu.temperatures && selectedMenu.temperatures.length > 0;
        const hasSize = selectedMenu.menu_sizes && selectedMenu.menu_sizes.length > 0;
        // 온도와 사이즈 모두 선택 가능한 경우
        if(hasTemp && hasSize) {
            if(!selectedTemperature || !selectedSize) {
                alert("모든 옵션을 선택해 주세요.");
                return;
            }
            finalName = `(${selectedTemperature}) ${selectedMenu.name} - ${selectedSize}`;
        }
        // 온도만 존재할 경우
        else if(hasTemp && !hasSize) {
            if(!selectedTemperature) {
                alert("온도를 선택해 주세요.");
                return;
            }
            finalName = `(${selectedTemperature}) ${selectedMenu.name}`;
        }
        // 사이즈만 존재할 경우
        else if(!hasTemp && hasSize) {
            if(!selectedSize) {
                alert("사이즈를 선택해 주세요.");
                return;
            }
            finalName = `${selectedMenu.name} - ${selectedSize}`;
        }
        // 둘 다 없는 경우
        else {
            finalName = selectedMenu.name;
        }
        // 최종 메뉴명 input에 값 채우기
        document.getElementById('final-menu-name').value = finalName;

        // 모든 모달 닫기
        closeDetailModal();
        closeMenuModal();
    }
});

// (선택 사항) 모달 외부 클릭 시 모달 닫기
window.addEventListener('click', function(e) {
    const menuModal = document.getElementById('menuModal');
    const detailModal = document.getElementById('detailModal');
    if(e.target === menuModal) {
        closeMenuModal();
    }
    if(e.target === detailModal) {
        closeDetailModal();
    }
});