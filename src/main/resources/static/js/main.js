/********************************************************************
 * main.js
 *
 * – 모든 기능을 모듈별로 정리 (Common, UIHandlers, OrderModule,
 *   MenuModule, MemberModule)
 * – async/await를 사용하여 비동기 API 호출 후 순차적(동기적)으로 처리
 * – 각 버튼 클릭 및 모달 관련 이벤트 핸들러들을 함수로 모듈화
 ********************************************************************/

// -----------------------
// Common Utility Module
// -----------------------
const Common = (() => {
    function getCorpBrand() {
        const path = window.location.pathname.toLowerCase();
        if (path.includes('mammoth-coffee')) {
            return 'MAMMOTH_COFFEE';
        } else if (path.includes('mammoth-express')) {
            return 'MAMMOTH_EXPRESS';
        }
        return '';
    }
    return { getCorpBrand };
})();

// -----------------------
// Loading Indicator Module
// -----------------------
const Loading = (() => {
    const loadingArea = document.getElementById('loading-area');
    function show() {
        loadingArea.classList.remove('hidden');
    }
    function hide() {
        loadingArea.classList.add('hidden');
    }
    return { show, hide };
})();

// -----------------------
// UI Handlers (탭 전환 등)
// -----------------------
const UIHandlers = (() => {
    const menuAreaBtn = document.getElementById("menu-area-btn");
    const orderListBtn = document.getElementById("order-list-btn");
    const unorderableMenuBtn = document.getElementById("unorderable-menu-btn");
    const menuArea = document.getElementById("menu-area");
    const orderListArea = document.getElementById("order-list-area");
    const memberListArea = document.getElementById("member-list-area");
    const finalMenuNameInput = document.getElementById('final-menu-name');
    const unorderableMenuArea = document.getElementById("unorderable-menu-area");

    function setRecentMemberAndMenu() {
        const recentMemberId = localStorage.getItem(Common.getCorpBrand() + "-member-id");
        const recentMemberName = localStorage.getItem(Common.getCorpBrand() + "-member-name");
        const recentMenuName = localStorage.getItem(Common.getCorpBrand() + "-menu-name");

        if (recentMemberId) {
            document.getElementById('member-selected-id').value = recentMemberId;
        }
        if (recentMemberName) {
            document.getElementById('member-search-input').value = recentMemberName;
        }
        if (recentMenuName) {
            document.getElementById('final-menu-name').value = recentMenuName;
        }
    }

    function setActive(button) {

        // 기본 스타일 초기화
        menuAreaBtn.className = "rounded-md cursor-pointer px-2 py-2 text-sm font-semibold text-blue-500 border-2 border-blue-500 flex-auto";
        orderListBtn.className = "rounded-md cursor-pointer px-2 py-2 text-sm font-semibold text-blue-500 border-2 border-blue-500 flex-auto";
        unorderableMenuBtn.className = "rounded-md cursor-pointer px-2 py-2 text-sm font-semibold text-pink-500 border-2 border-pink-500 flex-auto";

        // 모든 영역 숨김
        menuArea.classList.add("hidden");
        orderListArea.classList.add("hidden");
        memberListArea.classList.add("hidden");
        unorderableMenuArea.classList.add("hidden");

        if (button === "menu") {
            menuAreaBtn.className = "rounded-md cursor-pointer px-2 py-2 bg-blue-500 text-sm text-white font-semibold flex-auto";
            menuArea.classList.remove("hidden");
        } else if (button === "order") {
            orderListBtn.className = "rounded-md cursor-pointer px-2 py-2 bg-blue-500 text-sm text-white font-semibold flex-auto";
            orderListArea.classList.remove("hidden");
        } else if (button === "unorderable") {
            unorderableMenuBtn.className = "rounded-md cursor-pointer px-2 py-2 bg-pink-500 text-sm text-white font-semibold flex-auto";
            unorderableMenuArea.classList.remove("hidden");
            // 주문불가 메뉴 리스트 로드
            UnorderableMenuModule.loadUnorderableMenus();
        }
    }

    function handleMenuAreaClick() {
        setActive("menu");
    }

    function handleOrderListClick() {
        setActive("order");
    }

    function handleUnorderableMenuClick() {
        setActive("unorderable");
    }

    function handleOrderPassClick() {
        finalMenuNameInput.value = "PASS";
    }

    function renderFavoriteMenuButton() {
        let htmlContent = '';
        favoriteMenus.forEach(menu => {
            htmlContent += `<div class="col-span-3 text-center flex">`;
            htmlContent += `    <button id="favorite-${menu.id}" class="rounded-md cursor-pointer border-2 border-blue-800 text-blue-800 px-4 py-2 text-sm font-semibold flex-auto">${menu.name}</button>`;
            htmlContent += `</div>`;
        })
        if(favoriteMenus.length%2 === 1){
            htmlContent += `<div class="col-span-3 text-center flex"></div>`
        }
        document.getElementById('favorite-menu-area').innerHTML = htmlContent;
        favoriteMenus.forEach(menu => {
            const favoriteId = `favorite-${menu.id}`;
            document.getElementById(favoriteId).addEventListener('click', () => MenuModule.openDetailModal(menu));
        });
    }

    return {
        init: function() {
            setRecentMemberAndMenu();
            renderFavoriteMenuButton();
            menuAreaBtn.addEventListener('click', handleMenuAreaClick);
            orderListBtn.addEventListener('click', handleOrderListClick);
            unorderableMenuBtn.addEventListener('click', handleUnorderableMenuClick);
            document.getElementById('order-pass-btn').addEventListener('click', handleOrderPassClick);
        }
    };
})();

// -----------------------
// Order Module
// -----------------------
const OrderModule = (() => {
    let todayOrderData = [];
    const orderTotalCount = document.getElementById('order-total-count');
    let viewAllOrderMembersBtn = document.getElementById('view-all-order-members-btn');

    async function fetchOrders() {
        try {
            const corpBrand = Common.getCorpBrand();
            const response = await fetch(`/api/v1/order-histories?start_date=${today}&end_date=${today}&brand=${corpBrand}`);
            if (!response.ok) throw new Error('Failed to fetch orders');
            todayOrderData = await response.json();
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    }

    async function fetchOrdersNotYet() {
        try {
            const corpBrand = Common.getCorpBrand();
            const response = await fetch(`/api/v1/order-histories/not-yet?start_date=${today}&end_date=${today}&brand=${corpBrand}`);
            if (!response.ok) throw new Error('Failed to fetch not-yet orders');
            const data = await response.json();
            let htmlContent = '';
            data.forEach(memberName => {
                htmlContent += `<button class="rounded-full px-2 bg-pink-300 text-xs font-semibold">${memberName}</button>`;
            });
            document.getElementById('order-not-yet-area').innerHTML = htmlContent;
        } catch (error) {
            console.error('Error fetching orders not yet:', error);
        }
    }

    async function setOrderTotalCount() {
        // await new Promise(resolve => setTimeout(resolve, 200));
        orderTotalCount.innerText = `총합 = ${todayOrderData.length}개`;
    }

    async function renderOrderSummary() {
        // await new Promise(resolve => setTimeout(resolve, 200));
        const groupedOrders = {};
        todayOrderData.forEach(order => {
            const menu = order.menu_name;
            if (!groupedOrders[menu]) {
                groupedOrders[menu] = { count: 0, members: new Set() };
            }
            groupedOrders[menu].count += 1;
            groupedOrders[menu].members.add(order.member_name);
        });

        let summaryArray = Object.keys(groupedOrders).map(menu => ({
            menu_name: menu,
            count: groupedOrders[menu].count,
            members: Array.from(groupedOrders[menu].members)
        }));

        summaryArray.forEach(item => {
            item.members.sort((a, b) => a.localeCompare(b));
        });

        summaryArray.sort((a, b) => {
            if (a.menu_name === 'PASS' && b.menu_name !== 'PASS') return 1;
            if (b.menu_name === 'PASS' && a.menu_name !== 'PASS') return -1;
            if (b.count !== a.count) return b.count - a.count;
            return a.menu_name.localeCompare(b.menu_name);
        });

        let htmlContent = '';
        summaryArray.forEach(item => {
            htmlContent += `<div class="col-span-6 text-sm flex">
                        <button class="view-order-members-btn rounded-xl bg-stone-100 px-1 cursor-pointer">▶</button> 
                        ${item.menu_name} = ${item.count}개
                      </div>`;
            htmlContent += `<div class="col-span-6 border-b py-4 border-gray-200 flex flex-wrap hidden">`;
            item.members.forEach(memberName => {
                htmlContent += `<button class="rounded-full px-2 bg-blue-300 text-xs font-semibold">${memberName}</button>`;
            });
            htmlContent += `</div>`;
        });
        document.getElementById('order-summary').innerHTML = htmlContent;

        let viewOrderMembersBtnClass = document.querySelectorAll('.view-order-members-btn');
        viewOrderMembersBtnClass.forEach(button => {
            button.addEventListener('click', function(e) {
                const currentButton = e.currentTarget;
                const parentDiv = currentButton.parentElement;
                const targetDiv = parentDiv.nextElementSibling;
                if (currentButton.textContent.trim() === '▶') {
                    currentButton.textContent = '▼';
                    if (targetDiv && targetDiv.classList.contains('hidden')) {
                        targetDiv.classList.remove('hidden');
                    }
                } else {
                    viewAllOrderMembersBtn.textContent = "모두펼치기";
                    currentButton.textContent = '▶';
                    if (targetDiv && !targetDiv.classList.contains('hidden')) {
                        targetDiv.classList.add('hidden');
                    }
                }

                let openAll = false;
                for (let i = 0; i < viewOrderMembersBtnClass.length; i++) {
                    const button = viewOrderMembersBtnClass[i];
                    if(button.textContent.trim() === '▶'){
                        openAll = true;
                        break;
                    }
                }
                if(openAll){
                    viewAllOrderMembersBtn.textContent = "모두펼치기";
                }else{
                    viewAllOrderMembersBtn.textContent = "모두접기";
                }

            });
        });


    }

    async function refreshOrderList() {
        const seoulDate = new Date().toLocaleDateString("en-CA", {
            timeZone: "Asia/Seoul"
        });
        if(seoulDate!==today){
            window.location.reload();
        }
        Loading.show();
        await fetchOrders();
        await fetchOrdersNotYet();
        await setOrderTotalCount();
        await renderOrderSummary();
        Loading.hide();
    }

    async function setAction(){
        document.getElementById('order-list-refresh-area').addEventListener("click", async function(e) {
            e.stopPropagation();
            await refreshOrderList();
        });

        viewAllOrderMembersBtn.addEventListener("click", async function(e) {
            e.stopPropagation();

            const allOrderMembers = document.querySelectorAll('.view-order-members-btn');
            let openAll = false;
            for (let i = 0; i < allOrderMembers.length; i++) {
                const button = allOrderMembers[i];
                if(button.textContent.trim() === '▶'){
                    openAll = true;
                    break;
                }
            }
            // let openAll = allOrderMembers.some(button => button.textContent.trim() === '▶');
            if(openAll){
                viewAllOrderMembersBtn.textContent = "모두접기";
                allOrderMembers.forEach(button => {
                    button.textContent = '▼';
                    const targetDiv = button.parentElement.nextElementSibling;
                    if (targetDiv && targetDiv.classList.contains('hidden')) {
                        targetDiv.classList.remove('hidden');
                    }
                });

            } else {
                viewAllOrderMembersBtn.textContent = "모두펼치기";
                allOrderMembers.forEach(button => {
                    button.textContent = '▶';
                    const targetDiv = button.parentElement.nextElementSibling;
                    if (targetDiv && !targetDiv.classList.contains('hidden')) {
                        targetDiv.classList.add('hidden');
                    }
                });
            }

        })
    }

    return {
        init: async function() {
            await fetchOrders();
            await fetchOrdersNotYet();
            await setOrderTotalCount();
            await renderOrderSummary();
            await setAction();
        },
        refresh: refreshOrderList
    };
})();

// -----------------------
// Menu Module
// -----------------------
const MenuModule = (() => {
    let menus = [];
    let selectedMenu = null;
    let selectedTemperature = null;
    let selectedSize = null;

    async function fetchMenus() {
        try {
            const corpBrand = Common.getCorpBrand();
            const response = await fetch(`/api/v1/menus/${corpBrand}`);
            if (!response.ok) throw new Error('Failed to fetch menus');
            menus = await response.json();
        } catch (error) {
            console.error('Error fetching menus:', error);
        }
    }

    function renderCategories() {
        const categorySet = new Set();
        menus.forEach(menu => categorySet.add(menu.category_name));
        const categories = Array.from(categorySet);
        const container = document.getElementById('category-buttons');
        container.innerHTML = '';
        categories.forEach(category => {
            const btn = document.createElement('button');
            btn.textContent = category;
            btn.className = "cursor-pointer px-4 py-2 rounded border border-gray-300";
            btn.addEventListener('click', () => loadMenuItems(category));
            container.appendChild(btn);
        });
    }

    function loadMenuItems(category) {
        const grid = document.getElementById('menu-grid');
        grid.innerHTML = '';
        const filteredMenus = menus.filter(menu => menu.category_name === category);
        filteredMenus.forEach(menu => {
            const div = document.createElement('div');
            div.className = "cursor-pointer border rounded border-gray-100 overflow-hidden transition-shadow";
            div.innerHTML = `
          <img src="${menu.image_url}" alt="${menu.name}" class="w-full h-32 object-cover">
          <div class="p-2 text-center font-medium">${menu.name}</div>
      `;
            div.addEventListener('click', () => openDetailModal(menu));
            grid.appendChild(div);
        });
    }

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
        document.getElementById('detailImage').src = menu.image_url;
        document.getElementById('menu-detail-name').innerText = menu.name;
        // 온도 옵션 생성
        const tempContainer = document.getElementById('temperature-options');
        const tempOptionsDiv = tempContainer.querySelector('.flex');
        if (menu.temperatures && menu.temperatures.length > 0) {
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

        // 사이즈 옵션 생성
        const sizeContainer = document.getElementById('size-options');
        const sizeOptionsDiv = sizeContainer.querySelector('.flex');
        if (menu.menu_sizes && menu.menu_sizes.length > 0) {
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

        const detailModal = document.getElementById('detailModal');
        detailModal.classList.remove('hidden');
        detailModal.classList.add('flex');
    }
    function closeDetailModal() {
        const detailModal = document.getElementById('detailModal');
        detailModal.classList.add('hidden');
        detailModal.classList.remove('flex');
    }

    function confirmSelection() {
        if (selectedMenu) {
            let finalName = "";
            const hasTemp = selectedMenu.temperatures && selectedMenu.temperatures.length > 0;
            const hasSize = selectedMenu.menu_sizes && selectedMenu.menu_sizes.length > 0;
            if (hasTemp && hasSize) {
                if (!selectedTemperature || !selectedSize) {
                    alert("모든 옵션을 선택해 주세요.");
                    return;
                }
                finalName = `(${selectedTemperature}) ${selectedMenu.name} - ${selectedSize}`;
            } else if (hasTemp && !hasSize) {
                if (!selectedTemperature) {
                    alert("온도를 선택해 주세요.");
                    return;
                }
                finalName = `(${selectedTemperature}) ${selectedMenu.name}`;
            } else if (!hasTemp && hasSize) {
                if (!selectedSize) {
                    alert("사이즈를 선택해 주세요.");
                    return;
                }
                finalName = `${selectedMenu.name} - ${selectedSize}`;
            } else {
                finalName = selectedMenu.name;
            }
            document.getElementById('final-menu-name').value = finalName;
            closeDetailModal();
            closeMenuModal();
        }
    }

    function initMenuModule() {
        document.getElementById('menu-list').addEventListener('click', async function() {
            await fetchMenus();
            renderCategories();
            const defaultCategory = "커피";
            if(menus.some(menu => menu.category_name === defaultCategory)) {
                loadMenuItems(defaultCategory);
            } else if (menus.length > 0) {
                loadMenuItems(menus[0].category_name);
            }
            openMenuModal();
        });
        document.getElementById('close-menu-modal-btn').addEventListener('click', closeMenuModal);
        document.getElementById('close-detail-modal-btn').addEventListener('click', closeDetailModal);
        document.getElementById('confirmSelection').addEventListener('click', confirmSelection);

        window.addEventListener('click', function(e) {
            const menuModal = document.getElementById('menuModal');
            const detailModal = document.getElementById('detailModal');
            if(e.target === menuModal) closeMenuModal();
            if(e.target === detailModal) closeDetailModal();
        });
    }
    return { init: initMenuModule, openDetailModal: openDetailModal };
})();

// -----------------------
// Member Module
// -----------------------
const MemberModule = (() => {
    let membersData = [];
    const memberInput = document.getElementById('member-search-input');
    const memberList = document.getElementById('member-options-list');
    const selectedMemberId = document.getElementById('member-selected-id');
    const toggleArrow = document.getElementById('member-toggle-arrow');
    const refreshBtn = document.getElementById('member-refresh-btn');
    const memberModificationBtn = document.getElementById('member-modification-btn');
    const memberListArea = document.getElementById('member-list-area');

    async function fetchMembers() {
        try {
            const response = await fetch('/api/v1/members');
            if (!response.ok) throw new Error('Failed to fetch members');
            membersData = await response.json();
            renderMemberOptions(membersData);
        } catch (error) {
            console.error('Error fetching members:', error);
        }
    }

    function renderMemberOptions(members) {
        memberList.innerHTML = '';
        members.forEach(member => {
            const li = document.createElement('li');
            li.className = "cursor-pointer px-3 py-2";
            li.setAttribute('data-id', member.id);
            li.textContent = member.name;
            memberList.appendChild(li);
        });
    }

    function filterMemberOptions() {
        const filter = memberInput.value.toLowerCase();
        const items = memberList.querySelectorAll('li');
        let hasVisible = false;
        items.forEach(item => {
            if(item.textContent.toLowerCase().includes(filter)) {
                item.style.display = 'block';
                hasVisible = true;
            } else {
                item.style.display = 'none';
            }
        });
        if(hasVisible) {
            memberList.classList.remove('hidden');
            toggleArrow.classList.add('rotate-180');
        } else {
            memberList.classList.add('hidden');
            toggleArrow.classList.remove('rotate-180');
        }
    }

    function showFullMemberList() {
        memberInput.value = '';
        memberList.querySelectorAll('li').forEach(item => {
            item.style.display = 'block';
        });
        memberList.classList.remove('hidden');
        toggleArrow.classList.add('rotate-180');
    }

    function hideMemberList() {
        memberList.classList.add('hidden');
        toggleArrow.classList.remove('rotate-180');
    }

    function initMemberEvents() {
        memberInput.addEventListener('input', filterMemberOptions);
        memberInput.addEventListener('focus', function(){
            if(memberList.classList.contains('hidden')){
                if(memberInput.value.trim()){
                    filterMemberOptions();
                } else {
                    showFullMemberList();
                }
            }
        });
        toggleArrow.addEventListener('click', function(e){
            e.stopPropagation();
            if(memberList.classList.contains('hidden')){
                showFullMemberList();
            } else {
                hideMemberList();
            }
        });
        memberList.addEventListener('click', function(e){
            if(e.target.tagName.toLowerCase() === 'li'){
                memberInput.value = e.target.textContent;
                selectedMemberId.value = e.target.getAttribute('data-id');
                hideMemberList();
            }
        });
        document.addEventListener('click', function(e){
            if(!e.target.closest('.member-select-wrapper')){
                hideMemberList();
            }
        });
        refreshBtn.addEventListener('click', async function(e){
            e.stopPropagation();
            Loading.show();
            hideMemberList();
            await fetchMembers();
            Loading.hide();
        });
        memberModificationBtn.addEventListener('click', async function() {
            document.getElementById('menu-area').classList.add("hidden");
            document.getElementById('order-list-area').classList.add("hidden");
            memberListArea.classList.remove("hidden");
            await fetchMembers();
            let htmlContent = '';
            membersData.forEach(member => {
                htmlContent += `<div class="col-span-6 grid grid-cols-6 gap-0 border-b border-gray-100">
                          <input type="hidden" value="${member.id}">
                          <div class="col-span-4 align-middle my-1">${member.name}</div>
                          <div class="col-span-2 flex justify-end">
                            <button class="delete-member-btn rounded-xl bg-pink-300 px-4 py-2 text-sm font-semibold cursor-pointer">삭제</button>
                          </div>
                        </div>`;
            });
            htmlContent += `<div class="col-span-6 py-4"></div>`;
            htmlContent += `<div class="col-span-6 grid grid-cols-24 gap-0 border-b border-gray-200">
                        <div class="col-span-3 my-1">이름</div>
                        <div class="col-start-4 col-span-16">
                          <div class="flex items-center bg-white pl-3">
                            <input type="text" id="create-member-name" class="block min-w-0 grow py-1.5 text-base text-gray-900 text-center placeholder:text-center placeholder:text-gray-400 focus:outline-none" placeholder="추가할 회원 이름을 입력">
                          </div>
                        </div>
                        <div class="col-start-20 col-span-5 flex justify-end">
                          <button id="create-member-btn" class="rounded-xl bg-blue-300 px-4 py-2 text-sm font-semibold cursor-pointer">추가</button>
                        </div>
                      </div>`;
            memberListArea.innerHTML = htmlContent;

            document.getElementById('create-member-name').focus();

            document.getElementById('create-member-name').addEventListener('keydown', function(event) {
                if (event.key === 'Enter') {
                    event.preventDefault(); // 기본 동작(예: 폼 제출) 방지
                    document.getElementById('create-member-btn').click();
                }
            });

            document.getElementById('create-member-btn').addEventListener("click", async function() {
                const memberNameInput = document.getElementById('create-member-name');
                if(!memberNameInput.value.trim()){
                    return;
                }
                const requestBody = { name: memberNameInput.value.trim() };
                Loading.show();
                try {
                    const response = await fetch('/api/v1/members', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(requestBody)
                    });
                    if (!response.ok) {
                        const errorText = await response.text();
                        const errorResponse = await fetch(`api/v1/errors/${errorText}`);
                        const errorData = await errorResponse.json();
                        alert(errorData.message);
                        Loading.hide();
                        return;
                    }
                    // alert('회원 등록이 완료되었습니다.');
                    Loading.hide();
                    memberModificationBtn.click();
                } catch (error) {
                    console.error('Error adding member:', error);
                    Loading.hide();
                }
            });

            document.querySelectorAll('.delete-member-btn').forEach(button => {
                button.addEventListener('click', async function(event) {
                    if (!confirm("해당 회원을 삭제하시겠습니까?")) return;
                    const parentDiv = event.target.closest('.grid');
                    const hiddenInput = parentDiv.querySelector('input[type="hidden"]');
                    const memberId = hiddenInput.value;
                    Loading.show();
                    try {
                        const response = await fetch(`/api/v1/members/${memberId}`, { method: 'DELETE' });
                        if (response.ok) {
                            alert('삭제가 완료되었습니다.');
                            Loading.hide();
                            memberModificationBtn.click();
                        } else {
                            Loading.hide();
                            alert('회원 삭제에 실패하였습니다.');
                        }
                    } catch (error) {
                        console.error('Error deleting member:', error);
                        Loading.hide();
                        alert('서버와 통신 중 오류가 발생하였습니다.');
                    }
                });
            });
        });
    }

    return { init: async function() {
            await fetchMembers();
            initMemberEvents();
        }
    };
})();


// -----------------------
// blacklist-menu Module
// -----------------------
const UnorderableMenuModule = (() => {
    let unorderableMenus = [];

    async function fetchUnorderableMenus() {
        try {
            const response = await fetch(`/api/v1/menus/blacklist/${Common.getCorpBrand()}`);
            if (!response.ok) throw new Error('Failed to fetch unorderable menus');
            unorderableMenus = await response.json();
        } catch(error) {
            console.error('Error fetching unorderable menus:', error);
        }
    }

    function renderUnorderableMenuList() {
        const container = document.getElementById('unorderable-menu-list');
        container.innerHTML = '';
        if (unorderableMenus.length === 0) {
            return;
        }
        unorderableMenus.forEach(menu => {
            const div = document.createElement('div');
            div.className = "col-span-6 grid grid-cols-6 gap-0 border-b border-gray-100";
            div.innerHTML = `
                <div class="col-span-4 align-middle my-1">${menu.name}</div>
                <div class="col-span-2 flex justify-end">
                    <button data-id="${menu.name}" class="delete-unorderable-menu-btn rounded-xl bg-pink-300 px-4 py-2 text-sm font-semibold cursor-pointer">삭제</button>
                </div>
            `;
            container.appendChild(div);
        });

        document.querySelectorAll('.delete-unorderable-menu-btn').forEach(button => {
            button.addEventListener('click', async () => {
                const blackName = button.getAttribute('data-id');
                if (!confirm("해당 메뉴를 주문불가 목록에서 삭제하시겠습니까?")) return;
                try {
                    const response = await fetch(`/api/v1/menus/blacklist/${Common.getCorpBrand()}/${blackName}`, { method: 'DELETE' });
                    if (response.ok) {
                        await loadUnorderableMenus();
                    } else {
                        alert('삭제에 실패했습니다.');
                    }
                } catch (error) {
                    console.error('Error deleting unorderable menu:', error);
                }
            });
        });
    }

    async function addUnorderableMenu() {
        const input = document.getElementById('new-unorderable-menu-name');
        const name = input.value.trim();
        if (!name) return;
        Loading.show();
        try {
            const response = await fetch(`/api/v1/menus/blacklist/${Common.getCorpBrand()}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name })
            });
            if (response.ok) {
                input.value = '';
                await loadUnorderableMenus();
                Loading.hide();
            } else {
                const errorText = await response.text();
                const errorResponse = await fetch(`api/v1/errors/${errorText}`);
                const errorData = await errorResponse.json();
                alert(errorData.message);
                Loading.hide();
            }
        } catch (error) {
            console.error('Error adding unorderable menu:', error);
        }
    }

    async function loadUnorderableMenus() {
        await fetchUnorderableMenus();
        renderUnorderableMenuList();
    }

    function init() {
        document.getElementById('add-unorderable-menu-btn').addEventListener('click', addUnorderableMenu);
    }

    return { init, loadUnorderableMenus };
})();



// -----------------------
// 주문하기 버튼 (전역 이벤트)
// -----------------------
document.getElementById('order-menu-btn').addEventListener("click", async function() {
    const seoulDate = new Date().toLocaleDateString("en-CA", {
        timeZone: "Asia/Seoul"
    });
    if(seoulDate!==today){
        alert("[한국시간 기준] 날짜가 변경되었으므로 새로고침 됩니다.");
        window.location.reload();
    }
    const memberSearchInput = document.getElementById("member-search-input");
    const memberSelectedId = document.getElementById("member-selected-id");
    if(!memberSearchInput.value.trim() || !memberSelectedId.value.trim()){
        alert("이름이 올바르게 선택되지 않았습니다.");
        return;
    }
    const corpBrand = Common.getCorpBrand();
    const memberId = parseInt(memberSelectedId.value, 10);
    const menuName = document.getElementById('final-menu-name').value.trim();
    const requestBody = {
        member_id: memberId,
        brand: corpBrand,
        menu_name: menuName
    };
    Loading.show();
    try {
        const response = await fetch('/api/v1/order-histories', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        if (!response.ok) {
            const errorText = await response.text();
            const errorResponse = await fetch(`api/v1/errors/${errorText}`);
            const errorData = await errorResponse.json();
            alert(errorData.message);
            Loading.hide();
            return;
        }

        localStorage.setItem(Common.getCorpBrand() + "-member-id", memberId);
        localStorage.setItem(Common.getCorpBrand() + "-member-name", memberSearchInput.value.trim());
        localStorage.setItem(Common.getCorpBrand() + "-menu-name", menuName);

        document.getElementById('order-list-refresh-area').click();
        document.getElementById('order-list-btn').click();
        Loading.hide();
    } catch (error) {
        console.error('주문 API 에러:', error);
        Loading.hide();
    }
});


//주문불가 메뉴명 입력후 엔터 시 버튼클릭으로 적용
document.getElementById('new-unorderable-menu-name').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // 기본 동작(예: 폼 제출) 방지
        document.getElementById('add-unorderable-menu-btn').click();
    }
});


// -----------------------
// 초기화: DOMContentLoaded 후 모듈 초기화
// -----------------------
document.addEventListener("DOMContentLoaded", async function() {
    UIHandlers.init();
    MenuModule.init();
    await MemberModule.init();
    await OrderModule.init();
    UnorderableMenuModule.init();
});