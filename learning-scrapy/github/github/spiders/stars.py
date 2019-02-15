# -*- coding: utf-8 -*-
import scrapy

# scrapy crawl stars
from github.items import GithubItem


class StarsSpider(scrapy.Spider):
    name = 'stars'
    allowed_domains = ['https://github.com/achievec?tab=stars']
    start_urls = ['https://github.com/achievec?tab=stars/']

    def parse(self, response):
        print(response.text)
        for div in response.css(
                '#js-pjax-container > div > div.col-9.float-left.pl-2 > div.position-relative > div > div > .col-12'):
            item = GithubItem()
            item.name = div.css("div.d-inline-block.mb-1 > h3 > a::attr(href)").get()
            response
            item.url = div.css("div.d-inline-block.mb-1 > h3 > a::attr(href)").get()
            item.stars = div.css("div.f6.text-gray.mt-2 > a.muted-link.mr-3")[1].css("::text")[1].get().strip().strip(
                "\n").strip(',')
            item.desc = div.css("div.py-1 > p::text")[1].get().strip()
            item.update_time = div.css("div.f6.text-gray.mt-2 > relative-time::attr('datetime')").get()
            yield item

        for pagination in response.css(
                "#js-pjax-container > div > div.col-9.float-left.pl-2 > div.position-relative > div > div > .paginate-container a"):
            if pagination.css('::text').get() == 'Next':
                yield response.follow(response.urljoin(pagination.css('::attr(href)').get()), callback=self.parse)
