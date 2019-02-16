# -*- coding: utf-8 -*-
import scrapy
# scrapy crawl stars
from scrapy.selector import SelectorList

from github.items import GithubStarRepoItem


class StarsSpider(scrapy.Spider):
    name = 'stars'
    allowed_domains = ['github.com']
    start_urls = ['https://github.com/achievec?tab=stars']

    @staticmethod
    def extract_value(selector):
        if type(selector) == SelectorList:
            result = ''
            for i in selector:
                result += i.get().strip()
            # desc_temp = ''.join(desc_temp)
        else:
            result = selector.get().strip()
        return result

    def parse(self, response):
        star_repo_divs = response.css(
            '#js-pjax-container > div > div.col-9.float-left.pl-2 > div.position-relative > div > div > .col-12')
        for div in star_repo_divs:
            item = GithubStarRepoItem()
            item['name'] = div.css("div.d-inline-block.mb-1 > h3 > a::attr(href)").get()[1:]
            item['url'] = response.urljoin(div.css("div.d-inline-block.mb-1 > h3 > a::attr(href)").get())
            item['stars'] = int(StarsSpider.extract_value(
                div.css("div.f6.text-gray.mt-2 > a.muted-link.mr-3")[0].css("::text")).replace(',', ''))
            item['desc'] = StarsSpider.extract_value(div.css("div.py-1 > p::text"))
            item['update_time'] = div.css("div.f6.text-gray.mt-2 > relative-time::attr('datetime')").get()
            item['language'] = div.css("div.f6.text-gray.mt-2 > span[itemprop=programmingLanguage]::text").get()
            yield item

        next_div = response.css(
            "#js-pjax-container > div > div.col-9.float-left.pl-2 > div.position-relative > div > div > .paginate-container a")
        for pagination in next_div:
            if pagination.css('::text').get() == 'Next':
                yield response.follow(response.urljoin(pagination.css('::attr(href)').get()), callback=self.parse)
