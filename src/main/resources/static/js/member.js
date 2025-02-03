document.addEventListener('DOMContentLoaded', function(){
    const memberInput      = document.getElementById('member-search-input');
    const memberList       = document.getElementById('member-options-list');
    const selectedMemberId = document.getElementById('member-selected-id');
    const toggleArrow      = document.getElementById('member-toggle-arrow');
    const refreshBtn       = document.getElementById('member-refresh-btn');

    let membersData = []; // API로 받아온 회원 데이터를 저장

    // API를 호출하여 회원 리스트를 받아오고, 옵션 리스트를 구성하는 함수
    function fetchMembers() {
        fetch('/api/v1/members')
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                membersData = data; // 전역 변수에 저장
                renderMemberOptions(membersData);
            })
            .catch(error => {
                console.error('회원 데이터를 가져오는 중 오류 발생:', error);
            });
    }

    // members 배열을 받아 <li> 태그들을 생성하여 memberList에 추가하는 함수
    function renderMemberOptions(members) {
        // 기존 항목 초기화
        memberList.innerHTML = '';
        members.forEach(member => {
            const li = document.createElement('li');
            li.className = "cursor-pointer px-3 py-2";
            li.setAttribute('data-id', member.id);
            li.textContent = member.name;
            memberList.appendChild(li);
        });
    }

    // 입력값에 따라 회원 옵션을 필터링하는 함수
    function filterMemberOptions(){
        const filter = memberInput.value.toLowerCase();
        const items = memberList.querySelectorAll('li');
        let hasVisible = false;
        items.forEach(item => {
            if(item.textContent.toLowerCase().includes(filter)){
                item.style.display = 'block';
                hasVisible = true;
            } else {
                item.style.display = 'none';
            }
        });
        if(hasVisible){
            memberList.classList.remove('hidden');
            toggleArrow.classList.add('rotate-180');
        } else {
            memberList.classList.add('hidden');
            toggleArrow.classList.remove('rotate-180');
        }
    }

    // 전체 회원 리스트를 펼치면서 입력 필드를 초기화하는 함수
    function showFullMemberList(){
        memberInput.value = ''; // 입력값 초기화 → 전체 리스트 노출
        memberList.querySelectorAll('li').forEach(item => {
            item.style.display = 'block';
        });
        memberList.classList.remove('hidden');
        toggleArrow.classList.add('rotate-180');
    }

    // 리스트를 숨기는 함수
    function hideMemberList(){
        memberList.classList.add('hidden');
        toggleArrow.classList.remove('rotate-180');
    }

    // 이벤트: 회원 이름 타이핑 시 옵션 필터링
    memberInput.addEventListener('input', filterMemberOptions);

    // 이벤트: 입력 필드 포커스 시, 입력값에 따른 필터링 또는 전체 리스트 표시
    memberInput.addEventListener('focus', function(){
        if(memberList.classList.contains('hidden')){
            if(memberInput.value.trim()){
                filterMemberOptions();
            } else {
                showFullMemberList();
            }
        }
    });

    // 이벤트: 화살표 아이콘 클릭 시 전체 리스트 토글 (보이기/숨기기)
    toggleArrow.addEventListener('click', function(e){
        e.stopPropagation(); // 외부 클릭 이벤트와 충돌 방지
        if(memberList.classList.contains('hidden')){
            showFullMemberList();
        } else {
            hideMemberList();
        }
    });

    // 이벤트: 옵션 클릭 시 해당 회원 선택 및 히든 인풋에 id 저장
    memberList.addEventListener('click', function(e){
        if(e.target.tagName.toLowerCase() === 'li'){
            memberInput.value = e.target.textContent;
            selectedMemberId.value = e.target.getAttribute('data-id');
            hideMemberList();
        }
    });

    // 이벤트: 입력 영역 외부 클릭 시 리스트 숨김
    document.addEventListener('click', function(e){
        if(!e.target.closest('.member-select-wrapper')){
            hideMemberList();
        }
    });

    // 이벤트: 새로고침 버튼 클릭 시 회원 데이터를 다시 불러옴
    refreshBtn.addEventListener('click', function(e){
        loadingArea.classList.remove("hidden");
        e.stopPropagation(); // 외부 클릭 이벤트 방지
        hideMemberList();      // 새로고침 전에 리스트를 숨김 (원하는 경우)
        fetchMembers();
        loadingArea.classList.add("hidden");
    });

    memberModificationBtn.addEventListener("click", async function () {
        menuArea.classList.add("hidden");
        orderListArea.classList.add("hidden");
        memberListArea.classList.remove("hidden");

        await fetchMembers();
        let htmlContent = '';

        membersData.forEach(member => {
            htmlContent += `<div class="col-span-6 grid grid-cols-6 gap-0 border-b-1 border-gray-100">`;
            htmlContent += `    <input type="hidden" value="${member.id}"><div class="col-span-4 align-middle my-1">${member.name}</div><div class="col-span-2 flex justify-end"><button class="delete-member-btn rounded-xl bg-pink-300 px-4 py-2 text-sm font-semibold cursor-pointer">삭제</button></div>`;
            htmlContent += `</div>`;
        });
        htmlContent += `<div class="col-span-6 py-4"></div>`;

        htmlContent += `<div class="col-span-6 grid grid-cols-24 gap-0 border-b-1 border-gray-200">`;
        htmlContent += `    <div class="col-span-3 my-1">이름</div>`;
        htmlContent += `    <div class="col-start-4 col-span-16">`;
        htmlContent += `        <div class="flex items-center bg-white pl-3">`;
        htmlContent += `            <div class="shrink-0 text-base text-gray-500 select-none sm:text-sm/6"></div>`;
        htmlContent += `            <input type="text" id="create-member-name" class="block min-w-0 grow py-1.5 text-base text-gray-900 text-center placeholder:text-center placeholder:text-gray-400 focus:outline-none sm:text-sm/6" placeholder="추가할 회원 이름을 입력">`;
        htmlContent += `        </div>`;
        htmlContent += `    </div>`;
        htmlContent += `    <div class="col-start-20 col-span-5 flex justify-end">`;
        htmlContent += `        <button id="create-member-btn" class="rounded-xl bg-blue-300 px-4 py-2 text-sm font-semibold cursor-pointer">추가</button>`;
        htmlContent += `    </div>`;
        htmlContent += `</div>`;

        document.getElementById('member-list-area').innerHTML = htmlContent;

        document.getElementById('create-member-btn').addEventListener("click", async function() {
            const memberNameInput = document.getElementById('create-member-name');
            if(memberNameInput.value==='' || memberNameInput.value===null || memberNameInput.value===undefined){
                return;
            }
            const requestBody = {
                name : memberNameInput.value
            }

            loadingArea.classList.remove("hidden");
            try {
                const response = await fetch('/api/v1/members', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(requestBody)
                });

                if (!response.ok) {
                    response.text().then(value => {
                        fetch(`api/v1/errors/${value}`)
                            .then(response => response.json())
                            .then(data => {
                                alert(data.message);
                            })
                            .catch(error => {})
                    })
                    loadingArea.classList.add("hidden");
                    return;
                }

                alert('회원 등록이 완료되었습니다.');
                loadingArea.classList.add("hidden");
                //통신 성공 후 리스트 클릭
                window.location.reload();

            }catch (error) {
                console.error('주문 API 에러:', error);
                loadingArea.classList.add("hidden");
            }

        })

        document.querySelectorAll('.delete-member-btn').forEach(button => {
            button.addEventListener('click', event => {
                // 확인 창 표시
                if (!confirm("해당 회원을 삭제하시겠습니까?")) {
                    return;  // 사용자가 취소하면 아무 작업도 수행하지 않음
                }

                // 클릭한 버튼의 부모 div를 찾고, 그 안의 hidden input에서 member id 추출
                const parentDiv = event.target.parentElement.parentElement;
                const hiddenInput = parentDiv.querySelector('input[type="hidden"]');
                const memberId = hiddenInput.value;

                loadingArea.classList.remove("hidden");
                // DELETE 요청 전송
                fetch(`/api/v1/members/${memberId}`, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            alert('삭제가 완료되었습니다.');
                            loadingArea.classList.add("hidden");
                            window.location.reload()

                        } else {
                            loadingArea.classList.add("hidden");
                            alert('회원 삭제에 실패하였습니다.');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        loadingArea.classList.add("hidden");
                        alert('서버와 통신 중 오류가 발생하였습니다.');
                    });
            });
        });

    });

    // 페이지 로딩 시 회원 데이터를 API에서 가져옴
    fetchMembers();
});