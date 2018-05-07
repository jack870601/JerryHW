# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Mother(models.Model):
	content = models.CharField(max_length=1000)

	def __str__(self):
		return self.content

# Create your models here.
