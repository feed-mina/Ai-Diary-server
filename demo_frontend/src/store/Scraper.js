const puppeteer = require('puppeteer');
const puppeteerConfig = require()

async function scrapeDiscounts() {
    const browser = await puppeteer.launch();
    const page = await browser.newPage();

    // cu 웹 사이트 할인 정보 크롤링
    await page.goto('https://cu.co.kr/event/plus.do');
    const cuDiscounts = await page.evaluate(() => {
        const discounts = [];
        document.querySelectorAll('.proList .name').forEach(item => discounts.push({
            store: 'CU',
            title: item.innerText,
            details: 'Buy one get one free'
        }));
    });
    return discounts;
    // gs25, skt, kt , LG U+도 유사한 방식으로 크롤링
    // 여러 웹사이트의 할인 정보를 합쳐서 반환
    const allDiscounts = [...cuDiscounts]; //실제로는 다른 사이트 결과도 합쳐야 한다.
    await browser.close();
    return allDiscounts;
}

// 할인 정보는 DB에 저장하거나 바로 API에서 제공할 수 있다.