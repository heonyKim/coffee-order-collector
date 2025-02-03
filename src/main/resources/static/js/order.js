document.addEventListener('DOMContentLoaded', function(){

    const orderTotalCount       = document.getElementById('order-total-count');
    let todayOrderData = []; // API로 받아온 회원 데이터를 저장

    // API를 호출하여 주문 리스트를 받아옴
    function fetchOrders() {
        return new Promise((resolve) => {
            const corpBrand = getCorpBrand();
            fetch(`/api/v1/order-histories?start_date=${today}&end_date=${today}&brand=${corpBrand}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('네트워크 응답이 올바르지 않습니다.');
                    }
                    return response.json();
                })
                .then(data => {
                    todayOrderData = data; // 전역 변수에 저장
                    console.log("data : ", data);

                })
                .catch(error => {
                    console.error('주문내역 데이터를 가져오는 중 오류 발생:', error);
                });
            resolve();
        });
    }

    // API를 호출하여 주문하지 않은 회원의 리스트를 받아옴
    function fetchOrdersNotYet() {
        return new Promise((resolve) => {
            const corpBrand = getCorpBrand();
            fetch(`/api/v1/order-histories/not-yet?start_date=${today}&end_date=${today}&brand=${corpBrand}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('네트워크 응답이 올바르지 않습니다.');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("not-yet : ", data);
                    let htmlContent = '';
                    data.forEach(memberName => {
                        htmlContent += `<button class="rounded-full px-2 bg-pink-300 text-xs font-semibold">${memberName}</button>`
                    })
                    // 6. id가 order-not-yet-area인 div에 결과 출력
                    document.getElementById('order-not-yet-area').innerHTML = htmlContent;

                })
                .catch(error => {
                    console.error('미주문자 회원 내역 데이터를 가져오는 중 오류 발생:', error);
                });
            resolve();
        });
    }


    function setOrderTotalCount() {
        return new Promise((resolve) => {
            setTimeout(() => {
                orderTotalCount.innerText = `총합 = ${todayOrderData.length}개`;
                resolve();
            }, 200)
        });
    }

    function orderSummary(){
        return new Promise((resolve) => {
            setTimeout(() => {
                // 1. 메뉴별로 그룹화: 각 메뉴의 주문 수와 회원명을 수집 (회원명은 중복 제거)
                let groupedOrders = {}; //주문 취합
                todayOrderData.forEach(order => {
                    const menu = order.menu_name;
                    if (!groupedOrders[menu]) {
                        groupedOrders[menu] = { count: 0, members: new Set() };
                    }
                    groupedOrders[menu].count += 1;
                    groupedOrders[menu].members.add(order.member_name);
                });
                console.log("groupedOrders",groupedOrders)

                // 2. 그룹 데이터를 배열로 변환 및 회원명 Set을 배열로 변경
                let summaryArray = Object.keys(groupedOrders).map(menu => ({
                    menu_name: menu,
                    count: groupedOrders[menu].count,
                    members: Array.from(groupedOrders[menu].members)
                }));

                // 3. 각 그룹 내의 회원명을 사전순(오름차순) 정렬
                summaryArray.forEach(item => {
                    item.members.sort((a, b) => a.localeCompare(b));
                });

                // 4. 그룹들을 "주문 수 내림차순, 같다면 메뉴명 오름차순" 정렬
                summaryArray.sort((a, b) => {
                    // 만약 a가 "PASS"이고 b가 "PASS"가 아니라면 a를 뒤로 보냄
                    if (a.menu_name === 'PASS' && b.menu_name !== 'PASS') return 1;
                    // 만약 b가 "PASS"이고 a가 "PASS"가 아니라면 b를 뒤로 보냄
                    if (b.menu_name === 'PASS' && a.menu_name !== 'PASS') return -1;

                    if (b.count !== a.count) {
                        return b.count - a.count;
                    }
                    return a.menu_name.localeCompare(b.menu_name);
                });

                console.log("summaryArray",summaryArray);

                // 5. HTML 문자열로 변환 (Tailwind CSS 클래스를 이용하여 간격(mb-2) 등 스타일 적용)
                let htmlContent = '';
                summaryArray.forEach(item => {
                    htmlContent += `<div class="col-span-6 text-sm flex"> <button class="view-order-members-btn rounded-xl bg-stone-100 px-1 cursor-pointer">▶</button> ${item.menu_name} = ${item.count}개</div>`;
                    //(${item.members.join(', ')})
                    htmlContent += `<div class="col-span-6 border-b-1 py-4 border-gray-200 flex flex-wrap hidden">`;
                    item.members.forEach(memberName => {
                        htmlContent += `<button class="rounded-full px-2 bg-blue-300 text-xs font-semibold">${memberName}</button>`
                    })
                    htmlContent += `</div>`;
                });

                // 6. id가 order-summary인 div에 결과 출력
                document.getElementById('order-summary').innerHTML = htmlContent;

                // 모든 view-order-members-btn 클래스를 가진 버튼에 이벤트 등록
                document.querySelectorAll('.view-order-members-btn').forEach(button => {
                    button.addEventListener('click', function(e) {
                        // 클릭된 버튼 참조
                        const currentButton = e.currentTarget;

                        // 부모 div (버튼이 포함된)
                        const parentDiv = currentButton.parentElement;
                        // 부모 div 바로 다음 형제 div (주문 상세를 담은 div)
                        const targetDiv = parentDiv.nextElementSibling;

                        // 버튼의 텍스트에 따라 처리
                        if (currentButton.textContent.trim() === '▶') {
                            // ▶이면 ▼로 변경
                            currentButton.textContent = '▼';
                            // targetDiv에서 hidden 클래스 제거
                            if (targetDiv && targetDiv.classList.contains('hidden')) {
                                targetDiv.classList.remove('hidden');
                            }
                        } else if (currentButton.textContent.trim() === '▼') {
                            // ▼이면 ▶로 변경
                            currentButton.textContent = '▶';
                            // targetDiv에 hidden 클래스 추가
                            if (targetDiv && !targetDiv.classList.contains('hidden')) {
                                targetDiv.classList.add('hidden');
                            }
                        }
                    });
                });
                resolve();
            },200)
        })
    }

    fetchOrdersNotYet();
    fetchOrders()
        .then(() => {
            setOrderTotalCount();
            orderSummary();

            document.getElementById('order-list-refresh-area').addEventListener("click", async function (e) {
                loadingArea.classList.remove("hidden");
                e.stopPropagation();
                await fetchOrders();
                fetchOrdersNotYet();
                await setOrderTotalCount();
                await orderSummary();
                loadingArea.classList.add("hidden");
            })
        })
        .catch(error => console.error(error));



});

