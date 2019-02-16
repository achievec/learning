# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class GithubStarRepoItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    # pass
    name = scrapy.Field()
    url = scrapy.Field()
    stars = scrapy.Field()
    update_time = scrapy.Field()
    desc = scrapy.Field()
    language = scrapy.Field()
