// 현재 URL에 따라 corpBrand 결정 (예: /mammoth-coffee → MAMMOTH_COFFEE)
function getCorpBrand() {
    const path = window.location.pathname.toLowerCase();
    if(path.includes('mammoth-coffee')) {
        return 'MAMMOTH_COFFEE';
    } else if(path.includes('mammoth-express')) {
        return 'MAMMOTH_EXPRESS';
    }
    return '';
}