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
            li.className = "cursor-pointer hover:bg-gray-200 px-3 py-2";
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
        e.stopPropagation(); // 외부 클릭 이벤트 방지
        hideMemberList();      // 새로고침 전에 리스트를 숨김 (원하는 경우)
        fetchMembers();
    });

    // 페이지 로딩 시 회원 데이터를 API에서 가져옴
    fetchMembers();
});