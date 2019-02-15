# -*- coding: utf-8 -*-
import scrapy


class StarsSpider(scrapy.Spider):
    name = 'stars'
    allowed_domains = ['https://github.com/achievec?tab=stars']
    start_urls = ['http://https://github.com/achievec?tab=stars/']

    def parse(self, response):
        pass
